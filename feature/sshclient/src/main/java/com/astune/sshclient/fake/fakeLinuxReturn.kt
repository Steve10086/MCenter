package com.astune.sshclient.fake

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

private val MutipleContent = "a"


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