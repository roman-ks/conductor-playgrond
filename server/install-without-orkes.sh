# Orkes has custom licence which needs to be evaluated.
# Therefore need to exclude io.orkes.* from the conductor-server artifact
# For this clone conductor repo, delete io.orkes dependencies and build a jar

export CONDUCTOR_VER=3.13.8
git clone https://github.com/Netflix/conductor.git
cd conductor
git checkout v$CONDUCTOR_VER
cd server
sed -i '/io.orkes./d' ./build.gradle
# build jar with a current version(we checked out to before) instead of letting plugin bump it
../gradlew bootJar -Prelease.useLastTag=true
mv build/libs/conductor-server-*.jar ../../
cd ../..
# can output an error deleting git file
rm conductor -fr

