#!/usr/bin/env bash
set -euo pipefail

# Runs all tests and coverage report for the project.
# Usage:
#   ./run-all-tests.sh
#   ./run-all-tests.sh --no-open
#   ./run-all-tests.sh --tests "com.acessibilidade.api.service.*Test"

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
MVNW="$ROOT_DIR/mvnw"
REPORT_PATH="$ROOT_DIR/target/site/jacoco/index.html"
OPEN_REPORT=true
TEST_PATTERN=""

while [[ $# -gt 0 ]]; do
  case "$1" in
    --no-open)
      OPEN_REPORT=false
      shift
      ;;
    --tests)
      TEST_PATTERN="${2:-}"
      if [[ -z "$TEST_PATTERN" ]]; then
        echo "[ERRO] Informe um padrão após --tests"
        exit 1
      fi
      shift 2
      ;;
    -h|--help)
      cat <<'EOF'
Executa testes e cobertura de uma so vez.

Opcoes:
  --no-open           Nao tenta abrir o relatorio HTML
  --tests "PADRAO"    Roda apenas classes de teste que combinam com o padrao Maven Surefire
  -h, --help          Mostra esta ajuda

Exemplos:
  ./run-all-tests.sh
  ./run-all-tests.sh --no-open
  ./run-all-tests.sh --tests "com.acessibilidade.api.service.*Test"
EOF
      exit 0
      ;;
    *)
      echo "[ERRO] Opcao invalida: $1"
      echo "Use --help para ver as opcoes."
      exit 1
      ;;
  esac
done

if [[ ! -x "$MVNW" ]]; then
  chmod +x "$MVNW"
fi

cd "$ROOT_DIR"

echo "[1/3] Limpando build anterior..."
if [[ -n "$TEST_PATTERN" ]]; then
  "$MVNW" -q clean
else
  "$MVNW" -q clean
fi

echo "[2/3] Executando testes e validacao de cobertura (verify)..."
if [[ -n "$TEST_PATTERN" ]]; then
  "$MVNW" verify -Dtest="$TEST_PATTERN"
else
  "$MVNW" verify
fi

echo "[3/3] Resultado"
echo "Relatorio JaCoCo: $REPORT_PATH"

if [[ -f "$REPORT_PATH" && "$OPEN_REPORT" == true ]]; then
  if command -v xdg-open >/dev/null 2>&1; then
    xdg-open "$REPORT_PATH" >/dev/null 2>&1 || true
    echo "Relatorio aberto no navegador (quando suportado)."
  else
    echo "xdg-open nao encontrado; abra manualmente o arquivo acima."
  fi
fi

echo "Concluido com sucesso."

