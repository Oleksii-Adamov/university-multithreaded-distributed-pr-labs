cmake_minimum_required(VERSION 3.15)
project(FoxAlgoCpp)

set(CMAKE_CXX_STANDARD 17)

add_executable(FoxAlgoCpp main.cpp util.h Algo.cpp Algo.h FoxProc.cpp FoxProc.h util.cpp CannonProc.cpp CannonProc.h TapeProc.cpp TapeProc.h Proc.h BlockProc.cpp BlockProc.h seqmult.h seqmult.cpp)
