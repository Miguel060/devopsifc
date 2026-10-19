#!/bin/sh
# Cria automaticamente o input GELF UDP no Graylog (sem intervencao manual)
API=http://graylog:9000/api
AUTH="admin:admin"

echo "Aguardando Graylog..."
until curl -sf "$API/system/lbstatus" >/dev/null; do sleep 5; done
echo "Graylog no ar."

if curl -s -u "$AUTH" "$API/system/inputs" | grep -q "GELF UDP"; then
  echo "Input GELF UDP ja existe."
  exit 0
fi

curl -sf -u "$AUTH" -H "X-Requested-By: init" -H "Content-Type: application/json" \
  -X POST "$API/system/inputs" -d '{
    "title": "GELF UDP",
    "type": "org.graylog2.inputs.gelf.udp.GELFUDPInput",
    "global": true,
    "configuration": {
      "bind_address": "0.0.0.0",
      "port": 12201,
      "recv_buffer_size": 262144,
      "decompress_size_limit": 8388608
    }
  }' && echo "Input GELF UDP criado."
