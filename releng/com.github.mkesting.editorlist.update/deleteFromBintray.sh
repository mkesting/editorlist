#!/bin/bash
#Sample Usage: deleteFromBintray.sh username apikey owner repo targetFolder pathToLocalP2Repo

BINTRAY_API=https://api.bintray.com/content
BINTRAY_USER=$1
BINTRAY_API_KEY=$2
BINTRAY_OWNER=$3
BINTRAY_REPO=$4
TARGET_FOLDER=$5
PATH_TO_LOCAL_P2_REPO=$6

CURL_USER="-u ${BINTRAY_USER}:${BINTRAY_API_KEY}"
CURL_BASEURL="${BINTRAY_API}/${BINTRAY_OWNER}/${BINTRAY_REPO}/${TARGET_FOLDER}"

function main() {
  delete_updatesite
}

function delete_updatesite() {
  echo "--------------------------------------------------------------------------------"
  echo "${BINTRAY_USER}"
  echo "${BINTRAY_API_KEY}"
  echo "${BINTRAY_OWNER}"
  echo "${BINTRAY_REPO}"
  echo "${TARGET_FOLDER}"
  echo "${PATH_TO_LOCAL_P2_REPO}"
  echo "--------------------------------------------------------------------------------"

  if [ ! -z "$PATH_TO_LOCAL_P2_REPO" ]; then
    cd "$PATH_TO_LOCAL_P2_REPO"
    if [ $? -ne 0 ]; then
      echo "$PATH_TO_LOCAL_P2_REPO does not exist"
      exit 1
    fi
  fi

  FILES=./*
  PLUGINDIR=./plugins/*
  FEATUREDIR=./features/*

  for f in $FILES; do
    if [ ! -d $f ]; then
      delete_file $f
    fi
  done

  for f in $FEATUREDIR; do
    delete_file $f
  done

  for f in $PLUGINDIR; do
    delete_file $f
  done

  echo "Done..."
}

function delete_file() {
  CURL_CMD="curl -X DELETE ${CURL_USER} ${CURL_BASEURL}/$1"
  echo "$CURL_CMD"
  eval "$CURL_CMD"
  echo ""
  echo "--------------------------------------------------------------------------------"
}

main "$@"
