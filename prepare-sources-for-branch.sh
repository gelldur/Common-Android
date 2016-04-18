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
com/dexode/util/log/Logger.java
com/dexode/util/log/FileDebugLog.java
com/dexode/service/WorkerService.java
com/dexode/service/RunnableWithParams.java
com/dexode/util/Utils.java
com/dexode/util/DeviceId.java
com/dexode/adapter/BaseAdapter.java
com/dexode/adapter/ViewHolderAdapterHelper.java
com/dexode/adapter/RecyclerAdapter.java
com/dexode/adapter/RecyclerAdapterCommandManager.java
com/dexode/adapter/holder/BaseHolder.java
com/dexode/util/Assert.java
com/dexode/util/DebugUtils.java
com/dexode/util/Validator.java
com/dexode/util/UtilsHash.java
com/dexode/util/RecyclerViewHelper.java
com/dexode/storage/SecurePreferences.java
okhttp3/interceptor/LogInterceptor.java
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
