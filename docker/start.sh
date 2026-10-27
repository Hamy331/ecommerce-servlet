#!/bin/sh
# Render cho biết cổng qua biến PORT, thông tin database qua DB_URL, DB_USER, DB_PASSWORD
set -e

CONF=/usr/local/tomcat/conf
PORT="${PORT:-8080}"
sed -i "s/port=\"8080\"/port=\"$PORT\"/" "$CONF/server.xml"

esc() {
    printf '%s' "$1" | sed -e 's/&/\&amp;/g' -e 's/</\&lt;/g' \
        -e 's/>/\&gt;/g' -e 's/"/\&quot;/g'
}

# thay cho META-INF/context.xml khi chạy trên server
mkdir -p "$CONF/Catalina/localhost"
cat > "$CONF/Catalina/localhost/ROOT.xml" <<XML
<?xml version="1.0" encoding="UTF-8"?>
<Context>
    <Resource name="jdbc/minishop" auth="Container"
        driverClassName="com.mysql.cj.jdbc.Driver"
        url="$(esc "$DB_URL")"
        username="$(esc "$DB_USER")" password="$(esc "$DB_PASSWORD")"
        maxTotal="10" maxIdle="5" maxWaitMillis="10000"
        testOnBorrow="true" validationQuery="SELECT 1"
        logAbandoned="true" removeAbandonedOnBorrow="true"
        removeAbandonedTimeout="60" type="javax.sql.DataSource" />
</Context>
XML

exec catalina.sh run
