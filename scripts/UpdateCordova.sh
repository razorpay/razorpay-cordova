# to ensure that the script fails in case any command in the script does

set -e

read -p "Enter the path of the cordova directory : " CORDOVA_DIRECTORY_PATH

if [ "$CORDOVA_DIRECTORY_PATH" == "" ]
 then 
 cd ..	
 echo "current dir is $(pwd)"
 CORDOVA_DIRECTORY_PATH="$(pwd)/"
fi 

read -p "Enter the latest version of the iOS framework :" IOS_NEW_RELEASE_VERSION

read -p "Enter the latest version of the cordova package :" CORDOVA_NEW_RELEASE_VERSION

if [ IOS_NEW_RELEASE_VERSION == "" ] || [ CORDOVA_NEW_RELEASE_VERSION == "" ]
 then 
 exit 1
fi

# split version into components

function decrementedVersion() {

	IFS='.' read -a ARR_VERSION_COMPONENTS <<< "$1"
	ARRAY_COUNT=${#ARR_VERSION_COMPONENTS[@]}
	REVISION_VERSION=${ARR_VERSION_COMPONENTS[ARRAY_COUNT - 1]}
	DECREMENTED_REVISION_VERSION=$(($REVISION_VERSION - 1))
	echo "${ARR_VERSION_COMPONENTS[0]}.${ARR_VERSION_COMPONENTS[1]}.$DECREMENTED_REVISION_VERSION"

}

IOS_OLD_RELEASE_VERSION=$( decrementedVersion "$IOS_NEW_RELEASE_VERSION" )
CORDOVA_OLD_RELEASE_VERSION=$( decrementedVersion "$CORDOVA_NEW_RELEASE_VERSION" )

echo "ios old release version : $IOS_OLD_RELEASE_VERSION \n ios new release version : $IOS_NEW_RELEASE_VERSION \n cordova old release version : $CORDOVA_OLD_RELEASE_VERSION \n cordova new release version :$CORDOVA_NEW_RELEASE_VERSION"

# create a new branch and make the changes

git checkout master
git pull origin master
git checkout -b r/v"$CORDOVA_NEW_RELEASE_VERSION"

# download and replace framework

echo "\nDownloading file from URL:https://rzp-mobile.s3.amazonaws.com/ios/checkout/$IOS_NEW_RELEASE_VERSION/RazorpayBitcode.framework.zip"
wget https://rzp-mobile.s3.amazonaws.com/ios/checkout/"$IOS_NEW_RELEASE_VERSION"/RazorpayBitcode.framework.zip
unzip RazorpayBitcode.framework.zip
cp -R Razorpay.framework ./src/ios/
   # r for recursive i.e for directories , f makes rm consider a success if the file it is trying to delete is not found , as in the
   # of __MACOSX - a macos dependant file which is generated only when unzipped from iOS 11 zips and not from iOS 8
rm -rf RazorpayBitcode*.framework.zip Razorpay.framework/ __MACOSX/

# update package.json

sed -i '' "s/$CORDOVA_OLD_RELEASE_VERSION/$CORDOVA_NEW_RELEASE_VERSION/g" package.json

# push code , create tag and create PR

git add .
git commit -m "iOS Release $CORDOVA_NEW_RELEASE_VERSION"
git push origin r/v"$CORDOVA_NEW_RELEASE_VERSION"
hub pull-request -F- <<< "iOS Release $CORDOVA_NEW_RELEASE_VERSION
updated framework for version $CORDOVA_NEW_RELEASE_VERSION"
git tag -a "v$CORDOVA_NEW_RELEASE_VERSION" -m "tagging version $CORDOVA_NEW_RELEASE_VERSION"
git push origin "v$CORDOVA_NEW_RELEASE_VERSION"

