package com.astune.sshclient.fake

import com.astune.data.respository.ShellContentRepository
import com.astune.model.ssh.ShellContent
import com.astune.model.ssh.ShellState

fun getContent(): ShellContent {
    val content = ShellContent(windowsHeight = 36)
    val repo = ShellContentRepository(ShellState())
    repo.decode(
        content, Content1)
    return content
}

fun getMeasureContent():ShellContent{
    val content = ShellContent()
    val repo = ShellContentRepository(ShellState())
    repo.decode(content, ColContent)
    repo.decode(content, LineContent)
    return content
}

fun getNmtuiContent(): ShellContent {
    val repo = ShellContentRepository(ShellState())
    val content = ShellContent(windowsHeight = 30)
    repo.decode(
        content, Nmtui)
    return content
}

fun getTestContent(): ShellContent {
    val repo = ShellContentRepository(ShellState())
    val content = ShellContent(windowsHeight = 30)
    repo.decode(content, FirstContent)
    repo.decode(content, CommandTest)
    repo.decode(content, BackSpace)
    repo.decode(content, MutipleContent)
    repo.decode(content, MutipleContent)
    repo.decode(content, MutipleContent)
    repo.decode(content, moveTo(1,4))
    repo.decode(content, BackSpace)
    repo.decode(content, moveTo(1, 12))
    repo.decode(content, moveTo(1, 112))
    return content
}

fun getTopContent(): ShellContent {
    val content = ShellContent(windowsHeight = 33)
    val repo = ShellContentRepository(ShellState())

    repo.decode(content, TopFunction)
    repo.decode(content, TopFunction)
    repo.decode(content, "a")
    repo.decode(content, TopFunctionRefresh)
    repo.decode(content, "\b\b\b\u001B[K????????????????????????")

    return content
}

private val MutipleContent = "a"

private fun moveTo(x:Int, y:Int) = "\u001B[$y;"+ x + "H"

private val TopFunction = "\u001B[H\u001B[J\u001B[m\u000Ftop - 06:25:29 up 17 days, \u001B[m\u000F\u001B[m\u000F\u001B[K\n" +
        "Tasks:\u001B[m\u000F\u001B[1m 109 \u001B[m\u000Ftotal,\u001B[m\u000F\u001B[1m   1 \u001B[m\u000Frunni\u001B[m\u000F\u001B[m\u000F\u001B[K\n" +
        "%Cpu(s):\u001B[m\u000F\u001B[1m  1.5 \u001B[m\u000Fus,\u001B[m\u000F\u001B[1m  2.9 \u001B[m\u000Fsy,\u001B[m\u000F\u001B[1m \u001B[m\u000F\u001B[m\u000F\u001B[K\n" +
        "MiB Mem :\u001B[m\u000F\u001B[1m    382.1 \u001B[m\u000Ftotal,\u001B[m\u000F\u001B[1m  \u001B[m\u000F\u001B[m\u000F\u001B[K\n" +
        "MiB Swap:\u001B[m\u000F\u001B[1m    191.0 \u001B[m\u000Ftotal,\u001B[m\u000F\u001B[1m  \u001B[m\u000F\u001B[m\u000F\u001B[K\n" +
        "\u001B[K\n" +
        "\u001B[7m    PID USER      PR  NI \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F  10017 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F  10081 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F\u001B[1m  10115 user      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F      1 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F      2 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F      3 root       0 -20 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F      4 root       0 -20 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F      8 root       0 -20 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F      9 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     10 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     11 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     12 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     13 root      rt   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     14 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     15 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     16 root      rt   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     17 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     20 root      20 \n" +
        "\u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     21 root      rt   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     22 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     25 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     26 root      rt   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     27 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     30 root      20   0 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     31 root       0 -20 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F     32 root       0 -20 \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000Froot      rt   0       0      0      0 S \u001B[m\u000F\u001B[K\n" +
        "\u001B[m\u000F 20   0  452800  13584   6472 S   1.6   3.5 \u001B[m\u000F\u001B[K\n" +
        "     0 S   0.0   0.0   0:00.75 ksoftirqd+ " +
        "    0 S   0.0   0.0   0:00.64 ksoftirqd+ "


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
        "\n" +
        "\u001B[6;1H\u001B[7m Unknown command - try 'h' for help \u001B[m\u000F\u001B[K"

private val FirstContent = "\u001B[1m\u001B[31mThe programs included with\u001B[38;2;25;10;74m the Debian GNU/Linux system are free software;\n" +
        "the exact distribution\u001B[m terms for each program are described in the\n" +
        "individual files in /usr/share/doc/*/copyright.\u001B[5;4H\n" +
        "Debian GNU/Linux comes with ABSOLUTELY NO WARRANTY, to the extent\n" +
        "permitted by applicable law.\n" +
        "Last login: Thu Feb 15 00:01:14 2024 from 172.26.131.104\n"

private val CommandTest ="\n\u001B[?2004huser@openstick:~\$ \n" +"\u001B[?2004huser@openstick:~\$ \n" +"\u001B[?2004huser@openstick:~\$ " +
                "vim NEW FILE\n" +
                "\u001B[?2004l\n" +
                "files to edit\b\n"+
                "\n" +
                "\n" +
                "\n" +
                "~\u001B[0m//\u001B[24;63H0,0-1\u001B[9CAll\u001B[1;1H"

private val BackSpace = "\b"


private val Content1 = "2 files to edit\n" +
        "\u001B[?1h\u001B=\n" +
        "\u001B[1;36r\u001B[m\u001B[m\u001B[0m\u001B[H\u001B[J\u001B[36;1H<W\" [New]\n" +
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
        "~\u001B[0m\u001B[36;21H0,0-1\u001B[9CAll\u001B[1;1H\n"

private val Nmtui = "\u001B[1;30r\u001B(B\u001B)0\u001B[m\u000F\u001B[1;30r\u001B[H\u001B[J\u001B[1;1H\u001B[10;11H\u000Elqu\u001B[m\u000F\u000F NetworkManager TUI \u000Etqqk\u001B[11;11Hx\u001B[25Cx\u001B[12;11Hx\u001B[m\u000F\u000F Please select an option \u000Ex\u001B[13;11Hx\u001B[25Cx\u001B[14;11Hx\u001B[m\u000F\u000F \u001B[1m\u001B[7mEdit a connection      \u001B[m\u000F \u000Ex\u001B[15;11Hx\u001B[m\u000F\u000F Activate a connection   \u000Ex\u001B[16;11Hx\u001B[m\u000F\u000F Set system hostname\u001B[5C\u000Ex\u001B[17;11Hx\u001B[25Cx\u001B[18;11Hx\u001B[m\u000F\u000F Quit\u001B[20C\u000Ex\u001B[19;11Hx\u001B[25Cx\u001B[20;11Hx\u001B[20C\u001B[m\u000F\u000F<OK> \u000Ex\u001B[21;11Hx\u001B[25Cx\u001B[22;11Hmqqqqqqqqqqqqqqqqqqqqqqqqqj\u001B[14;13H"

private val NmtuiExit = "[13;7H[K[14;7H[K[15;7H[K[16;7H[K[17;7H[K[18;7H[K[19;7H[K[20;7H[K[21;7H[K[22;7H[K[23;7H[K[24;7H[K[25;7H[K[36;1H[m "

private val LineContent = "\n2\n3\n4\n5\n6\n7\n8\n9\n10\n11\n12\n13\n14\n15\n16\n17\n18\n19\n20\n21\n22\n23\n24\n25\n26\n27\n28\n29\n30\n1\n" +
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

private val ColContent = "1234567890123456789012345678901234567890123456789012345678901234567890123456789"