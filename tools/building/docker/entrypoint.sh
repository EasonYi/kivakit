#!/bin/bash

source /root/.profile

echo " "
echo "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫ KivaKit Docker Build Help  ┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓"
echo "┋  "
echo "┋  Command                                       Description"
echo "┋  ------------------------------------------    ---------------------------"
echo "┋  kivakit-[tab]                                 see kivakit shell scripts"
echo "┋  kivakit-version.sh                            show kivakit version"
echo "┋  kivakit-build.sh                              build kivakit"
echo "┋  kivakit-git-pull.sh                           pull changes *"
echo "┋  kivakit-git-checkout.sh [branch]              check out the given branch *"
echo "┋  kivakit-docker-workspace.sh [host|container]  switch between host and container workspaces"
echo "┋  kivakit-feature-start.sh [branch]             start a feature branch *"
echo "┋  kivakit-feature-finish.sh [branch]            finish a feature branch *"
echo "┋"
echo "┋ * executes the command in each kivakit repository"
echo "┋"
echo "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛"
echo " "

$SHELL