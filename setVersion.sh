#! /bin/bash
sed -ri "1,/RE/ s/^(\s*version\s*=\s*)(.*)$/\1'$1'/" build.gradle