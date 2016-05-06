#!/bin/bash

INDEX=(1,2,3,4,5,17)
for i in ./import/datavalues.{11..20}.*.csv
do
	fname="${i##*/}"
	echo "converting ${fname}"

	grep "${VALUE}" "${i}" | cut -d, -f"${INDEX}" | while IFS=, read a b c d e f; do echo "${a//\"/},0.0,${c//\"/\'},${d//\"/},${e//\"/\'},${f//\"/}"; done | sed 's/\\//g' >> "./import_psql/${fname}"
done

