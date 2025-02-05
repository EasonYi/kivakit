#!/bin/bash

if [ -z "$PREBUILT" ]; then

    if [ ! -e "${HOME}/.m2/repository/com/telenav/cactus/cactus-build-metadata/${KIVAKIT_VERSION}/cactus-build-metadata-${KIVAKIT_VERSION}.jar" ]; then

        echo "Installing Cactus"

        cd "$CACTUS_HOME" || exit
        mvn --batch-mode clean install
    fi

    if [ ! -e "${HOME}/.m2/repository/com/telenav/kivakit/superpom/${KIVAKIT_VERSION}/superpom-${KIVAKIT_VERSION}.pom" ]; then

        echo "Installing KivaKit Superpom"

        cd "$KIVAKIT_HOME"/superpom || exit
        mvn --batch-mode --no-transfer-progress clean install

    fi

    export PREBUILT=true

fi

