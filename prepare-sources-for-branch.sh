#!/bin/bash
# Write here files or dirs you want to not remove
# Example
# Not remove file: src/com/squareup/picasso/CacheHelper.java
# Not remove  dir: src/com/dexode/fragment/
# You should update this script on your branch. This script should be executed after every pull.
# You can simply add git hook.
#
# How to add git hook: https://git-scm.com/book/en/v2/Customizing-Git-Git-Hooks
# 
 
NOT_REMOVED=(
# Add here files
)

cd ./src

for file in `git ls-files`; do
	git update-index --no-assume-unchanged $file
	if [ ! -f "$file" ]; then
		git checkout -- $file
	fi
	
	if [[ ${NOT_REMOVED[*]} =~ "$file" ]]
	then
		echo "Skipping $file"
	else
		echo "Processing $file"
		if [ -d "$file" ] ; then
			(cd $file && git update-index --assume-unchanged -- $(git ls-files '*'))
			rm -r $file
			rmdir --ignore-fail-on-non-empty --parents $file
		else
			git update-index --assume-unchanged $file
			rm -r $file
		fi
	fi
done
