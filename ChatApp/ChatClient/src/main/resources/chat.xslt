<?xml version="1.0" encoding="utf-8" ?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:template match="chat-conversation">
        <html lang="en">

            <head>
                <meta charset="UTF-8"/>
                <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
                <title>ITI Chat Application</title>
                <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"/>
                <link rel="stylesheet"
                      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.9.0/css/all.min.css"/>
                <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Abril+Fatface&amp;display=swap"/>
                <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto&amp;display=swap"/>
                <link rel="stylesheet" href="css/style.css"/>
            </head>

            <body>
                <div class="chat-app-wrapper">
                    <nav>
                        <div class="nav-left-section">
                            <img src="./assets/logo.svg" class="logo"/>
                            <span id="title">ChatApp</span>
                        </div>

                        <div class="nav-right-section">
                            <span class="welcome-messsage" id="loggedInUsername">
                                <xsl:value-of select="chatOwner"/>
                            </span>
                            <img id="loggedInUserAvatar"
                                 src="https://data.cometchat.com/assets/images/avatars/captainamerica.png"
                                 class="avatar"/>
                        </div>
                    </nav>

                    <div class="chat">
                        <div class="container">
                            <div class="msg-header">
                                <div class="active">
                                    <h5>
                                        <xsl:value-of select="chatName"/>
                                    </h5>
                                </div>
                            </div>

                            <div class="chat-page">
                                <div class="msg-inbox">
                                    <div class="chats" id="chats">
                                        <div class="msg-page" id="msg-page">
                                            <div>
                                                <div id="group-message-holder">

                                                    <xsl:variable name="owner" select="chatOwner"/>
                                                    <xsl:for-each select="//message">
                                                        <xsl:choose>
                                                            <xsl:when test="senderName=$owner">
                                                                <div class="outgoing-chats old-chats">
                                                                    <div class="outgoing-chats-msg">
                                                                        <p>
                                                                            <xsl:value-of select="content"/>
                                                                        </p>
                                                                    </div>
                                                                    <div class="outgoing-chats-img">
                                                                        <img src="https://data.cometchat.com/assets/images/avatars/captainamerica.png"
                                                                             alt="" class="avatar"/>
                                                                    </div>
                                                                </div>
                                                            </xsl:when>
                                                            <xsl:otherwise>
                                                                <div class="received-chats old-chats">
                                                                    <div class="received-chats-img">
                                                                        <img src="https://data.cometchat.com/assets/images/avatars/captainamerica.png"
                                                                             alt="Avatar" class="avatar"/>
                                                                    </div>

                                                                    <div class="received-msg">
                                                                        <div class="received-msg-inbox">
                                                                            <p>
                                                                                <span id="message-sender-id">
                                                                                    <xsl:value-of select="senderName"/>
                                                                                </span>
                                                                                <br/>
                                                                                <xsl:value-of select="content"/>
                                                                            </p>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </xsl:otherwise>
                                                        </xsl:choose>

                                                    </xsl:for-each>


                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <script src="https://code.jquery.com/jquery-3.4.1.min.js"
                        integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
                        crossorigin="anonymous"></script>
                <script type="text/javascript" src="https://unpkg.com/@cometchat-pro/chat@1.8/CometChat.js"></script>

            </body>

        </html>
    </xsl:template>

</xsl:stylesheet>
