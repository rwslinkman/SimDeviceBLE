#!/bin/zsh
# Make sure to install 'protoc' using 'brew install protobuf'
# Helpful article: https://www.baeldung.com/grpc-introduction#1-using-protocol-buffer-compiler
# Helpful article: https://developers.google.com/protocol-buffers/docs/kotlintutorial#compiling-your-protocol-buffers
print "SimDeviceBLE gRPC code generator"
print "Cleaning up generated code..."
rm -rf ./grpc/generated_java
rm -rf ./grpc/generated_kt
mkdir ./grpc/generated_java

print "Running code generator..."
protoc -I=./grpc --plugin=protoc-gen-grpc-java=./grpc/compiler/protoc-gen-grpc-java-1460 --java_out=./grpc/generated_java --grpc-java_out=./grpc/generated_java ./grpc/SimDeviceBLE.proto
print "Code generation completed!"

print "Applying generated code to project source"
rm -rf app/build/generated/src
mkdir -p ./app/build/generated/src/nl/rwslinkman/simdeviceble/grpc/server/
cp ./grpc/generated_java/nl/rwslinkman/simdeviceble/grpc/server/* app/build/generated/src/nl/rwslinkman/simdeviceble/grpc/server/
print "Copied files to 'app/build/generated/src' directory!"