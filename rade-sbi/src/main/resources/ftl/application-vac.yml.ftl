dows:
  vac:
    channel:
      hik:
        url: rtsp://admin:admin123@192.168.1.64:554/Streaming/Channels/101
        username: admin
        password: admin123
        mock: true
      mcgs:
        #url: sql://admin:admin123@192.168.1.64:554/Streaming/Channels/101
        #username: admin
        #password: admin123
        mock: true
        url: plc://s1200@192.168.111.224:102
        coreSize: 5
        delay: 1000


    init:
      #appId: 24Uk269Gu16vD19bv43
      appId: ${applicationVac.appId}