#!/bin/sh

echo Generate private key...
openssl genpkey -algorithm RSA -out rsa_private.pem -pkeyopt rsa_keygen_bits:2048

echo Generate public key...
openssl rsa -in rsa_private.pem -pubout -out rsa_public.pem
