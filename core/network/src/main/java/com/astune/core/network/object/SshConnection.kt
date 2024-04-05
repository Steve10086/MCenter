package com.astune.core.network.`object`

import net.schmizz.sshj.connection.channel.direct.Session
import net.schmizz.sshj.connection.channel.direct.Session.Shell

data class SshConnection(
    val shell: Shell,
    val session: Session
)
