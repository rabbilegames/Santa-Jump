rem get key
keytool -exportcert -alias androiddebugkey -keystore debug.keystore | openssl sha1 -binary | openssl base64

rem get App link
rem https://graph.facebook.com/app/app_link_hosts?access_token=737640846308076|4c7528ac62162fd672c37c85a9979aba&name=Peraliyan&android= [     {       url : "market://details?id=com.clozaps.christmassaga.free2",       package : "com.clozaps.christmassaga.free2",       app_name : "Santa Vs Grinch",     },  ]
pause