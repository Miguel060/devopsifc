#!/bin/sh
# Gera trafego continuo (2xx, 4xx, 5xx) para popular Grafana e Graylog
BASE=http://app:8080
while true; do
  curl -s -o /dev/null -X POST -H "Content-Type: application/json" -d '{"titulo":"tarefa gerada"}' $BASE/api/tarefas
  curl -s -o /dev/null $BASE/api/tarefas
  curl -s -o /dev/null $BASE/api/tarefas/999999
  curl -s -o /dev/null -X POST -H "Content-Type: application/json" -d '{"titulo":""}' $BASE/api/tarefas
  if [ $(( $(date +%s) % 5 )) -eq 0 ]; then curl -s -o /dev/null $BASE/api/demo/erro; fi
  sleep 1
done
