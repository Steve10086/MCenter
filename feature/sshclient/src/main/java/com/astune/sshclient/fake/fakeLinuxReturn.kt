package com.astune.sshclient.fake

import com.astune.data.utils.ANSICommendDecoder
import com.astune.data.utils.decode
import com.astune.model.ShellContent

fun getContent():ShellContent{
    val content = ShellContent()
    decode(
        content, MutipleContent)
    decode(
        content, MutipleContent)
    decode(
        content, MutipleContent)
    decode(
        content, Content1)
    return content
}

fun getTestContent():ShellContent{
    val content = ShellContent()
    val decoder = ANSICommendDecoder(content)
    decoder.decodeCommend(FirstContent)
    decoder.decodeCommend(Header)
    decoder.decodeCommend(CommandTest)
    decoder.decodeCommend(BackSpace)
    decoder.decodeCommend(MutipleContent)
    decoder.decodeCommend(MutipleContent)
    decoder.decodeCommend(MutipleContent)
    decoder.decodeCommend(moveTo(1,4))
    decoder.decodeCommend(BackSpace)
    decoder.decodeCommend(moveTo(1, 12))
    decoder.decodeCommend(Header)
    decoder.decodeCommend(moveTo(1, 112))
    return content
}

fun getTopContent():ShellContent{
    val content = ShellContent()
    decode(content, TopFunction)
    decode(content, TopFunctionRefresh)
    return content
}

private val MutipleContent = "a"

private fun moveTo(x:Int, y:Int) = "\u001B[$y;"+ x + "H"

private val TopFunction = "\u001B[H\u001B[J\u001B[m\u000Ftop - 00:37:27 up 4 days, 1\u001B[m\u000F\u001B[m\u000F\u001B[K\n" +
        "Tasks:\u001B[m\u000F\u001B[1m 109 \u001B[m\u000Ftotal,\u001B[m\u000F\u001B[1m   2 \u001B[m\u000Frunni\u001B[m\u000F\u001B[m\u000F\u001B[K\n" +
        "%Cpu(s):\u001B[m\u000F\u001B[1m  0.0 \u001B[m\u000Fus,\u001B[m\u000F\u001B[1m  5.6 \u001B[m\u000Fsy,\u001B[m\u000F\u001B[1m \u001B[m\u000F\u001B[m\u000F\u001B[K\n" +
        "MiB Mem :\u001B[m\u000F\u001B[1m    382.1 \u001B[m\u000Ftotal,\u001B[m\u000F\u001B[1m  \u001B[m\u000F\u001B[m\u000F\u001B[K\n" +
        "MiB Swap:\u001B[m\u000F\u001B[1m    191.0 \u001B[m\u000Ftotal,\u001B[m\u000F\u001B[1m  \u001B[m\u000F\u001B[m\u000F\u001B[K\n" +
        "\u001B[K\n" +
        "\u001B[7m    PID USER      PR  NI \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F    434 user      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F    273 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F    441 zerotie+  20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F    317 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F    227 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F      1 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F   3243 user      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F    284 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F   3240 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F    282 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F    265 systemd+  20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F    283 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F    279 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F    253 systemd+  20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F    396 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F   3264 user      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F    247 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F   3244 user      20   0 "

private val TopFunctionRefresh = "\u001B[H\u001B[m\u000Ftop - 07:35:09 up 5 days,  \u001B[m\u000F\u001B[m\u000F\u001B[K\n" +
        "\n" +
        "%Cpu(s): \u001B[m\u000F\u001B[1m  0.1\u001B[m\u000F/0.7  \u001B[m\u000F\u001B[m\u000F   1[\u001B[m\u000F\u001B[m\u000F\u001B[7m\u001B[m\u000F\u001B[m\u000F\u001B[m\u000F\u001B[m\u000F  \u001B[m\u000F\u001B[m\u000F\u001B[K\n" +
        "\n" +
        "\n" +
        "\u001B[K\n" +
        "\n" +
        "\u001B[m\u000F    273 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\n" +
        "\u001B[m\u000F    108 root      rt   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F   3407 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\n" +
        "\n"
        //"\u001B[J"

private val FirstContent = "\u001B[1m\u001B[31mThe programs included with\u001B[38;2;25;10;74m the Debian GNU/Linux system are free software;\n" +
        "the exact distribution\u001B[m terms for each program are described in the\n" +
        "individual files in /usr/share/doc/*/copyright.\u001B[5;4H\n" +
        "Debian GNU/Linux comes with ABSOLUTELY NO WARRANTY, to the extent\n" +
        "permitted by applicable law.\n" +
        "Last login: Thu Feb 15 00:01:14 2024 from 172.26.131.104\n"

private val Header = "\n\u001B[?2004huser@openstick:~\$ "

private val CommandTest ="\n\u001B[?2004huser@openstick:~\$ \n" +"\u001B[?2004huser@openstick:~\$ \n" +"\u001B[?2004huser@openstick:~\$ " +
                "vim NEW FILE\n" +
                "\u001B[?2004l\n" +
                "files to edit\b\n"+
                "\n" +
                "\n" +
                "\n" +
                "~\u001B[0m//\u001B[24;63H0,0-1\u001B[9CAll\u001B[1;1H"

private val BackSpace = "\b"


private val Content1 = "SMP PREEMPT Sun Feb 6 22:10:37 CST 2022 aarch64\n" +
                "\n" +
                "\u001B[1m\u001B[31mThe programs included with\u001B[38;2;25;10;74m the Debian GNU/Linux system are free software;\n" +
                "\u001B[mthe exact distribution terms for each program are described in the\n" +
                "individual files in /usr/share/doc/*/copyright.\n" +
                "\n" +
                "Debian GNU/Linux comes with ABSOLUTELY NO WARRANTY, to the extent\n" +
                "permitted by applicable law.\n" +
                "Last login: Thu Feb 15 00:01:14 2024 from 172.26.131.104\n" +
                "\u001B[?2004huser@openstick:~\$ \n" +
                "vim NEW FILE\n" +
                "\u001B[?2004l\n" +
                "files to edit\n" +
                "\u001B[?1h\u001B=\n" +
                "\u001B[1;24r\u001B[m\u001B[m\u001B[0m\u001B[H\u001B[J\u001B[24;1H\"NEW\" [New]\n" +
                "\u001B[2;1H\u001B[1m~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\n" +
                "~\u001B[0m//\u001B[24;63H0,0-1\u001B[9CAll\u001B[1;1H"

private val LineContent = "1\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20\n21\n22\n23\n24\n25\n26\n27\n28\n29\n30\n1\n" +
        "2\n" +
        "3\n" +
        "4\n" +
        "5\n" +
        "6\n" +
        "7\n" +
        "8\n" +
        "9\n" +
        "40\n"+
        "1\n" +
        "2\n" +
        "3\n" +
        "4\n" +
        "5\n" +
        "6\n" +
        "7\n" +
        "8\n" +
        "9\n" +
        "50\n"+
        "1\n" +
        "2\n" +
        "3\n" +
        "4\n" +
        "5\n" +
        "6\n" +
        "7\n" +
        "8\n" +
        "9\n" +
        "60"

private val ColContent = "01234567890123456789012345678901234567890123456789012345678901234567890123456789"