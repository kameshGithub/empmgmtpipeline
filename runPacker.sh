#!/usr/bin/env bash
set -e

	
run_packer() {
  set -x
echo "enter into run packer"
  packer build packer.json
  set +x
}

run_packer 
