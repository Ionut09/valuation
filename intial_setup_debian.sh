#!/usr/bin/env bash

apt install unrar -y
apt install maven -y

mkdir -p /data/jatodb
chmod 777 /data
chdmod 777 /data/jatodb
