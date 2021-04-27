#!/bin/bash

#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
#
#  © 2011-2021 Telenav, Inc.
#  Licensed under Apache License, Version 2.0
#
#///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

source library-functions.sh
source kivakit-projects.sh

help="[version]"

version=$1

require_variable version "$help"

for project_home in "${KIVAKIT_PROJECT_HOMES[@]}"; do

    git_flow_release_start $project_home $version

done
