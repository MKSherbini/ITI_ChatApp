<?xml version="1.0" encoding="utf-8" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="chat-conversation">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>Chat |
                    <xsl:value-of select="chatName"/>
                </title>
                <meta name="viewport" content="width=device-width, initial-scale=1"/>
            </head>
            <body>
                <h2>Notes Summary</h2>
                <table border="1">
                    <tr bgcolor="#0000ff">
                        <th>From</th>
                        <th>To</th>
                        <th>Body</th>
                    </tr>
                    <xsl:for-each select="//message">
                        <tr>
                            <td>
                                <xsl:value-of select="senderName"/>
                            </td>
                            <td>
                                <xsl:value-of select="receiverName"/>
                            </td>
                            <td>
                                <xsl:value-of select="content"/>
                            </td>

                        </tr>

                    </xsl:for-each>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
