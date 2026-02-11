# cbomkit-theia scan (generic)

`cbomkit-theia` scans a **directory** (or container image) for cryptographic assets (certs/keys/secrets/configs) and outputs a CycloneDX CBOM.

## Directory scan (Docker)

```zsh
# set what you want to scan and where to write the CBOM
SCAN_DIR="/absolute/path/to/something"
OUT_FILE="/absolute/path/to/cbom/output.cbom.json"

mkdir -p "$(dirname "$OUT_FILE")"

docker run --rm \
  -v "$SCAN_DIR:/scan:ro" \
  ghcr.io/cbomkit/cbomkit-theia:1.1.0 \
  dir /scan \
  > "$OUT_FILE"

ls -lh "$OUT_FILE" | cat
```

## Example (this repo)

```zsh
cd "$(git rev-parse --show-toplevel)"

mkdir -p cbom
docker run --rm \
  -v "$PWD/java-demo:/scan:ro" \
  ghcr.io/cbomkit/cbomkit-theia:1.1.0 \
  dir /scan \
  > cbom/java-demo.cbom.json

ls -lh cbom/java-demo.cbom.json | cat
```
