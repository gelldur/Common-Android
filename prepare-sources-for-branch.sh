# Write here files or dirs you want to remove
# Example
# Remove file: src/com/squareup/picasso/CacheHelper.java
# Remove  dir: src/com/dexode/fragment/
# You should update this script on your branch. This script should be executed after every pull.
# You can simply add git hook.
#
# How to add git hook: https://git-scm.com/book/en/v2/Customizing-Git-Git-Hooks
# 
 
IGNORED=(
./src/com/dexode/animation/interpolator/ReverseInterpolator.java
./src/com/dexode/storage/SecurePreferences.java
./src/com/dexode/util/DebugUtils.java
./src/com/dexode/util/LogUtils.java
./src/com/squareup/okhttp/interceptor/NetworkInterceptor.java
./src/com/squareup/okhttp/interceptor/LogInterceptor.java
)

for file in `git ls-files`; do
	git update-index --no-assume-unchanged $file
	if [ ! -f "$file" ]; then
		git checkout -- $file
	fi
done

for ignored in ${IGNORED[@]}; do
	echo "Processing: $ignored"
	if [ -d "$ignored" ] ; then
		(cd $ignored && git update-index --assume-unchanged -- $(git ls-files '*'))
		rm -r $ignored
		rmdir --ignore-fail-on-non-empty --parents $ignored
	else
		git update-index --assume-unchanged $ignored
		rm -r $ignored
	fi
done
