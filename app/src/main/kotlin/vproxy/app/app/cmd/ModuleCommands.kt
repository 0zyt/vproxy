package vproxy.app.app.cmd

import vproxy.app.app.cmd.handle.param.*
import vproxy.app.app.cmd.handle.resource.*
import vproxy.base.dns.Cache
import java.util.stream.Collectors

@Suppress("NestedLambdaShadowedImplicitParameter")
class ModuleCommands : Commands() {
  init {
    val it = AddHelper(resources)
    it + Res(ResourceType.tl) {
      it + ResAct(
        relation = ResourceType.tl,
        action = ActType.add,
        params = {
          it + ResActParam(Param.addr, true) { AddrHandle.check(it) }
          it + ResActParam(Param.ups, true)
          it + ResActParam(Param.aelg, false)
          it + ResActParam(Param.elg, false)
          it + ResActParam(Param.inbuffersize, false) { InBufferSizeHandle.check(it) }
          it + ResActParam(Param.outbuffersize, false) { OutBufferSizeHandle.check(it) }
          it + ResActParam(Param.timeout, false) { TimeoutHandle.check(it) }
          it + ResActParam(Param.protocol, false) { ProtocolHandle.check(it) }
          it + ResActParam(Param.ck, false)
          it + ResActParam(Param.secg, false)
        },
        exec = execUpdate { TcpLBHandle.add(it) },
      )
      it + ResAct(
        relation = ResourceType.tl,
        ActType.list,
      ) {
        val tlNames = TcpLBHandle.names()
        CmdResult(tlNames, tlNames, utilJoinList(tlNames))
      }
      it + ResAct(
        relation = ResourceType.tl,
        action = ActType.listdetail,
      ) {
        val tlRefList = TcpLBHandle.details()
        val tlRefStrList = tlRefList.stream().map { it.toString() }.collect(Collectors.toList())
        CmdResult(tlRefList, tlRefStrList, utilJoinList(tlRefList))
      }
      it + ResAct(
        relation = ResourceType.tl,
        action = ActType.update,
        params = {
          it + ResActParam(Param.inbuffersize) { InBufferSizeHandle.check(it) }
          it + ResActParam(Param.outbuffersize) { OutBufferSizeHandle.check(it) }
          it + ResActParam(Param.timeout) { TimeoutHandle.check(it) }
          it + ResActParam(Param.ck)
        },
        exec = execUpdate { TcpLBHandle.update(it) }
      )
      it + ResAct(
        relation = ResourceType.tl,
        action = ActType.remove,
        exec = execUpdate { TcpLBHandle.remove(it) }
      )
    }
    it + Res(ResourceType.socks5) {
      it + ResAct(
        relation = ResourceType.socks5,
        action = ActType.add,
        params = {
          it + ResActParam(Param.aelg)
          it + ResActParam(Param.elg)
          it + ResActParam(Param.addr, required) { AddrHandle.check(it) }
          it + ResActParam(Param.ups, required)
          it + ResActParam(Param.inbuffersize) { InBufferSizeHandle.check(it) }
          it + ResActParam(Param.outbuffersize) { OutBufferSizeHandle.check(it) }
          it + ResActParam(Param.timeout) { TimeoutHandle.check(it) }
          it + ResActParam(Param.secg)
        },
        flags = {
          it + ResActFlag(Flag.allownonbackend)
          it + ResActFlag(Flag.denynonbackend)
        },
        exec = execUpdate { Socks5ServerHandle.add(it) }
      )
      it + ResAct(
        relation = ResourceType.socks5,
        action = ActType.list
      ) {
        val socks5Names = Socks5ServerHandle.names()
        CmdResult(socks5Names, socks5Names, utilJoinList(socks5Names))
      }
      it + ResAct(
        relation = ResourceType.socks5,
        action = ActType.listdetail
      ) {
        val socks5RefList = Socks5ServerHandle.details()
        val socks5RefStrList = socks5RefList.stream().map { it.toString() }.collect(Collectors.toList())
        CmdResult(socks5RefList, socks5RefStrList, utilJoinList(socks5RefList))
      }
      it + ResAct(
        relation = ResourceType.socks5,
        action = ActType.update,
        params = {
          it + ResActParam(Param.inbuffersize) { InBufferSizeHandle.check(it) }
          it + ResActParam(Param.outbuffersize) { OutBufferSizeHandle.check(it) }
          it + ResActParam(Param.timeout) { TimeoutHandle.check(it) }
          it + ResActParam(Param.secg)
        },
        flags = {
          it + ResActFlag(Flag.allownonbackend)
          it + ResActFlag(Flag.denynonbackend)
        },
        exec = execUpdate { Socks5ServerHandle.update(it) }
      )
      it + ResAct(
        relation = ResourceType.socks5,
        action = ActType.remove,
        exec = execUpdate { Socks5ServerHandle.remove(it) }
      )
    }
    it + Res(ResourceType.dns) {
      it + ResAct(
        relation = ResourceType.dns,
        action = ActType.add,
        params = {
          it + ResActParam(Param.elg)
          it + ResActParam(Param.addr, required) { AddrHandle.check(it) }
          it + ResActParam(Param.ups, required)
          it + ResActParam(Param.ttl) { TTLHandle.check(it) }
          it + ResActParam(Param.secg)
        },
        exec = execUpdate { DNSServerHandle.add(it) }
      )
      it + ResAct(
        relation = ResourceType.dns,
        action = ActType.list,
        exec = {
          val dnsServerNames = DNSServerHandle.names()
          CmdResult(dnsServerNames, dnsServerNames, utilJoinList(dnsServerNames))
        }
      )
      it + ResAct(
        relation = ResourceType.dns,
        action = ActType.listdetail,
        exec = {
          val dnsServerRefList = DNSServerHandle.details()
          val dnsServerRefStrList = dnsServerRefList.stream().map { it.toString() }.collect(Collectors.toList())
          CmdResult(dnsServerRefStrList, dnsServerRefStrList, utilJoinList(dnsServerRefList))
        }
      )
      it + ResAct(
        relation = ResourceType.dns,
        action = ActType.update,
        params = {
          it + ResActParam(Param.ttl) { TTLHandle.check(it) }
          it + ResActParam(Param.secg)
        },
        exec = execUpdate { DNSServerHandle.update(it) }
      )
      it + ResAct(
        relation = ResourceType.dns,
        action = ActType.remove,
        exec = execUpdate { DNSServerHandle.remove(it) }
      )
    }
    it + Res(ResourceType.elg) {
      it + ResAct(
        relation = ResourceType.elg,
        action = ActType.add,
        exec = execUpdate { EventLoopGroupHandle.add(it) }
      )
      it + ResAct(
        relation = ResourceType.elg,
        action = ActType.list,
        exec = {
          val elgNames = EventLoopGroupHandle.names()
          CmdResult(elgNames, elgNames, utilJoinList(elgNames))
        }
      )
      it + ResAct(
        relation = ResourceType.elg,
        action = ActType.listdetail,
        exec = {
          val elgNames = EventLoopGroupHandle.names()
          CmdResult(elgNames, elgNames, utilJoinList(elgNames))
        }
      )
      it + ResAct(
        relation = ResourceType.elg,
        action = ActType.remove,
        check = { EventLoopGroupHandle.preRemoveCheck(it) },
        exec = execUpdate { EventLoopGroupHandle.remvoe(it) }
      )
    }
    it + Res(ResourceType.ups) {
      it + ResAct(
        relation = ResourceType.ups,
        action = ActType.add,
        exec = execUpdate { UpstreamHandle.add(it) }
      )
      it + ResAct(
        relation = ResourceType.ups,
        action = ActType.list,
        exec = {
          val upsNames = UpstreamHandle.names()
          CmdResult(upsNames, upsNames, utilJoinList(upsNames))
        }
      )
      it + ResAct(
        relation = ResourceType.ups,
        action = ActType.listdetail,
        exec = {
          val upsNames = UpstreamHandle.names()
          CmdResult(upsNames, upsNames, utilJoinList(upsNames))
        }
      )
      it + ResAct(
        relation = ResourceType.ups,
        action = ActType.remove,
        check = { UpstreamHandle.preRemoveCheck(it) },
        exec = execUpdate { UpstreamHandle.remove(it) }
      )
    }
    it + Res(ResourceType.sg) {
      it + ResAct(
        relation = ResourceType.sg,
        action = ActType.add,
        params = {
          it + ResActParam(Param.timeout, required)
          it + ResActParam(Param.period, required)
          it + ResActParam(Param.up, required)
          it + ResActParam(Param.down, required)
          it + ResActParam(Param.protocol)
          it + ResActParam(Param.meth) { MethHandle.get(it, "") }
          it + ResActParam(Param.anno) { AnnotationsHandle.check(it) }
          it + ResActParam(Param.elg)
        },
        check = { HealthCheckHandle.getHealthCheckConfig(it) },
        exec = execUpdate { ServerGroupHandle.add(it) }
      )
      it + ResAct(
        relation = ResourceType.sg,
        action = ActType.addto,
        targetRelation = ResRelation(ResourceType.ups),
        params = {
          it + ResActParam(Param.weight) { WeightHandle.check(it) }
          it + ResActParam(Param.anno) { AnnotationsHandle.check(it) }
        },
        exec = execUpdate { ServerGroupHandle.attach(it) }
      )
      it + ResAct(
        relation = ResourceType.sg,
        action = ActType.list,
        exec = {
          val sgNames = ServerGroupHandle.names()
          CmdResult(sgNames, sgNames, utilJoinList(sgNames))
        }
      )
      it + ResAct(
        relation = ResourceType.sg,
        action = ActType.listdetail,
        exec = {
          val refs = ServerGroupHandle.details()
          val refStrList = refs.stream().map { it.toString() }.collect(Collectors.toList())
          CmdResult(refs, refStrList, utilJoinList(refStrList))
        }
      )
      it + ResAct(
        relation = ResRelation(ResourceType.sg, ResRelation(ResourceType.ups)),
        action = ActType.list,
        exec = {
          val sgNames = ServerGroupHandle.names(it.resource.parentResource)
          CmdResult(sgNames, sgNames, utilJoinList(sgNames))
        }
      )
      it + ResAct(
        relation = ResRelation(ResourceType.sg, ResRelation(ResourceType.ups)),
        action = ActType.listdetail,
        exec = {
          val refs = ServerGroupHandle.details(it.resource.parentResource)
          val refStrList = refs.stream().map { it.toString() }.collect(Collectors.toList())
          CmdResult(refs, refStrList, utilJoinList(refStrList))
        }
      )
      it + ResAct(
        relation = ResourceType.sg,
        action = ActType.update,
        params = {
          it + ResActParam(Param.timeout) { HealthCheckHandle.getHealthCheckConfig(it) }
          it + ResActParam(Param.period) { HealthCheckHandle.getHealthCheckConfig(it) }
          it + ResActParam(Param.up) { HealthCheckHandle.getHealthCheckConfig(it) }
          it + ResActParam(Param.down) { HealthCheckHandle.getHealthCheckConfig(it) }
          it + ResActParam(Param.protocol)
          it + ResActParam(Param.meth) { MethHandle.get(it, "") }
          it + ResActParam(Param.anno) { AnnotationsHandle.check(it) }
        },
        exec = execUpdate { ServerGroupHandle.update(it) }
      )
      it + ResAct(
        relation = ResRelation(ResourceType.sg, ResRelation(ResourceType.ups)),
        action = ActType.update,
        params = {
          it + ResActParam(Param.weight) { WeightHandle.check(it) }
          it + ResActParam(Param.anno) { AnnotationsHandle.check(it) }
        },
        exec = execUpdate { ServerGroupHandle.updateInUpstream(it) }
      )
      it + ResAct(
        relation = ResourceType.sg,
        action = ActType.remove,
        check = { ServerGroupHandle.preRemoveCheck(it) },
        exec = execUpdate { ServerGroupHandle.remove(it) }
      )
      it + ResAct(
        relation = ResourceType.sg,
        action = ActType.removefrom,
        targetRelation = ResRelation(ResourceType.ups),
        exec = execUpdate { ServerGroupHandle.detach(it) }
      )
    }
    it + Res(ResourceType.el) {
      it + ResAct(
        relation = ResourceType.el,
        action = ActType.addto,
        targetRelation = ResRelation(ResourceType.elg),
        exec = execUpdate { EventLoopGroupHandle.add(it) }
      )
      it + ResAct(
        relation = ResRelation(ResourceType.el, ResRelation(ResourceType.elg)),
        action = ActType.list,
        exec = {
          val elNames = EventLoopHandle.names(it.resource.parentResource)
          CmdResult(elNames, elNames, utilJoinList(elNames))
        }
      )
      it + ResAct(
        relation = ResRelation(ResourceType.el, ResRelation(ResourceType.elg)),
        action = ActType.listdetail,
        exec = {
          val elNames = EventLoopHandle.names(it.resource.parentResource)
          CmdResult(elNames, elNames, utilJoinList(elNames))
        }
      )
      it + ResAct(
        relation = ResourceType.el,
        action = ActType.removefrom,
        targetRelation = ResRelation(ResourceType.elg),
        exec = execUpdate { EventLoopHandle.remove(it) }
      )
    }
    it + Res(ResourceType.svr) {
      it + ResAct(
        relation = ResourceType.svr,
        action = ActType.addto,
        targetRelation = ResRelation(ResourceType.sg),
        params = {
          it + ResActParam(Param.addr, required) { AddrHandle.check(it) }
          it + ResActParam(Param.weight) { WeightHandle.check(it) }
        },
        exec = execUpdate { ServerHandle.add(it) }
      )
      it + ResAct(
        relation = ResRelation(ResourceType.svr, ResRelation(ResourceType.sg)),
        action = ActType.list,
        exec = {
          val serverNames = ServerHandle.names(it.resource.parentResource)
          CmdResult(serverNames, serverNames, utilJoinList(serverNames))
        }
      )
      it + ResAct(
        relation = ResRelation(ResourceType.svr, ResRelation(ResourceType.sg)),
        action = ActType.list,
        exec = {
          val svrRefList = ServerHandle.detail(it.resource.parentResource)
          val svrRefStrList = svrRefList.stream().map { it.toString() }
            .collect(Collectors.toList())
          CmdResult(svrRefList, svrRefStrList, utilJoinList(svrRefList))
        }
      )
      it + ResAct(
        relation = ResRelation(ResourceType.svr, ResRelation(ResourceType.sg)),
        action = ActType.update,
        params = {
          it + ResActParam(Param.weight) { WeightHandle.check(it) }
        },
        exec = execUpdate { ServerHandle.update(it) }
      )
      it + ResAct(
        relation = ResourceType.svr,
        action = ActType.removefrom,
        targetRelation = ResRelation(ResourceType.sg),
        exec = execUpdate { ServerHandle.remove(it) }
      )
    }
    it + Res(ResourceType.secg) {
      it + ResAct(
        relation = ResourceType.secg,
        action = ActType.add,
        params = {
          it + ResActParam(Param.secgrdefault, required) { SecGRDefaultHandle.check(it) }
        },
        exec = execUpdate { SecurityGroupHandle.add(it) }
      )
      it + ResAct(
        relation = ResourceType.secg,
        action = ActType.list,
        exec = {
          val sgNames = SecurityGroupHandle.names()
          CmdResult(sgNames, sgNames, utilJoinList(sgNames))
        }
      )
      it + ResAct(
        relation = ResourceType.secg,
        action = ActType.listdetail,
        exec = {
          val secg = SecurityGroupHandle.detail()
          val secgStrList = secg.stream().map { it.toString() }
            .collect(Collectors.toList())
          CmdResult(secgStrList, secgStrList, utilJoinList(secg))
        }
      )
      it + ResAct(
        relation = ResourceType.secg,
        action = ActType.update,
        params = {
          it + ResActParam(Param.secgrdefault, required) { SecGRDefaultHandle.check(it) }
        },
        exec = execUpdate { SecurityGroupHandle.update(it) }
      )
      it + ResAct(
        relation = ResourceType.secg,
        action = ActType.remove,
        check = { SecurityGroupHandle.preRemoveCheck(it) },
        exec = execUpdate { SecurityGroupHandle.remove(it) }
      )
    }
    it + Res(ResourceType.secgr) {
      it + ResAct(
        relation = ResourceType.secgr,
        action = ActType.addto,
        targetRelation = ResRelation(ResourceType.secg),
        params = {
          it + ResActParam(Param.net, required) { NetworkHandle.check(it) }
          it + ResActParam(Param.protocol, required) { ProtocolHandle.check(it) }
          it + ResActParam(Param.portrange, required) { PortRangeHandle.check(it) }
          it + ResActParam(Param.secgrdefault, required) { SecGRDefaultHandle.check(it) }
        },
        exec = execUpdate { SecurityGroupRuleHandle.add(it) }
      )
      it + ResAct(
        relation = ResRelation(ResourceType.secgr, ResRelation(ResourceType.secg)),
        action = ActType.list,
        exec = {
          val ruleNames = SecurityGroupRuleHandle.names(it.resource.parentResource)
          CmdResult(ruleNames, ruleNames, utilJoinList(ruleNames))
        }
      )
      it + ResAct(
        relation = ResRelation(ResourceType.secgr, ResRelation(ResourceType.secg)),
        action = ActType.list,
        exec = {
          val rules = SecurityGroupRuleHandle.detail(it.resource.parentResource)
          val ruleStrList = rules.stream().map { it.toString() }
            .collect(Collectors.toList())
          CmdResult(rules, ruleStrList, utilJoinList(rules))
        }
      )
      it + ResAct(
        relation = ResourceType.secgr,
        action = ActType.removefrom,
        targetRelation = ResRelation(ResourceType.secg),
        exec = execUpdate { SecurityGroupRuleHandle.remove(it) }
      )
    }
    it + Res(ResourceType.ck) {
      it + ResAct(
        relation = ResourceType.ck,
        action = ActType.add,
        params = {
          it + ResActParam(Param.cert, required)
          it + ResActParam(Param.key, required)
        },
        exec = execUpdate { CertKeyHandle.add(it) }
      )
      it + ResAct(
        relation = ResourceType.ck,
        action = ActType.list,
        exec = {
          val names = CertKeyHandle.names()
          CmdResult(names, names, utilJoinList(names))
        }
      )
      it + ResAct(
        relation = ResourceType.ck,
        action = ActType.listdetail,
        exec = {
          val names = CertKeyHandle.names()
          CmdResult(names, names, utilJoinList(names))
        }
      )
      it + ResAct(
        relation = ResourceType.ck,
        action = ActType.remove,
        check = { CertKeyHandle.preRemoveCheck(it) },
        exec = execUpdate { CertKeyHandle.remove(it) }
      )
    }
    it + Res(ResourceType.dnscache) {
      it + ResAct(
        relation = ResourceType.dnscache,
        action = ActType.list,
        check = { ResolverHandle.checkResolver(it.resource.parentResource) },
        exec = {
          val cacheCnt = DnsCacheHandle.count()
          CmdResult(cacheCnt, cacheCnt, "" + cacheCnt)
        }
      )
      it + ResAct(
        relation = ResRelation(ResourceType.dnscache, ResRelation(ResourceType.resolver)),
        action = ActType.listdetail,
        check = { ResolverHandle.checkResolver(it.resource.parentResource) },
        exec = {
          val caches = DnsCacheHandle.detail()
          val cacheStrList = caches.stream().map { c: Cache ->
            listOf(
              c.host,
              c.ipv4.stream().map { it.formatToIPString() }
                .collect(Collectors.toList()),
              c.ipv6.stream().map { it.formatToIPString() }
                .collect(Collectors.toList())
            )
          }.collect(Collectors.toList())
          CmdResult(caches, cacheStrList, utilJoinList(caches))
        }
      )
      it + ResAct(
        relation = ResourceType.dnscache,
        action = ActType.remove,
        check = { ResolverHandle.checkResolver(it.resource.parentResource) },
        exec = execUpdate { DnsCacheHandle.remove(it) }
      )
    }
  } // end init

  private fun utilJoinList(ls: List<*>): String {
    val sb = StringBuilder()
    var isFirst = true
    for (o in ls) {
      if (isFirst) {
        isFirst = false
      } else {
        sb.append("\n")
      }
      sb.append(o)
    }
    return sb.toString()
  }
}
