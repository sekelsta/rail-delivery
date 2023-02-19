#!/usr/bin/python3

import os
import shutil
import sys

lwjgl_libs = ['lwjgl', 'lwjgl-glfw', 'lwjgl-opengl']
lwjgl_directory = 'libs/lwjgl3'
other_libs = ['libs/*']
main_class = 'traingame.Main'
test_class = 'traingame.test.Main'
misc_compiler_args = '-Xlint:unchecked'
output_directory = 'bin'
source_directory = 'src'

def join_paths(root, *paths):
    for path in paths:
        root = os.path.join(root, path)
    return root

classpath = ''
for lib in lwjgl_libs:
    # os.pathsep is ';' on Windows or ':' on Linux.
    # Not to be confused with os.path.sep, which is '/' or '\'
    classpath += join_paths(lwjgl_directory, lib, '*') + os.pathsep
for lib in other_libs:
    classpath += lib + os.pathsep

def clean():
    if os.path.isdir(output_directory):
        shutil.rmtree(output_directory)

def count():
    total_lines = 0;
    num_files = 0;
    for root, dirs, files in os.walk(source_directory):
        for f in files:
            if f.endswith('.java'):
                fio = open(os.path.join(root, f), 'r')
                line_count = len(fio.readlines())
                print(str(f) + ': ' + str(line_count))
                total_lines += line_count
                num_files += 1
    print('Number of files: ' + str(num_files))
    print('Total lines of code: ' + str(total_lines))

def build():
    global misc_compiler_args
    clean()
    if not os.path.isdir(output_directory):
        os.mkdir(output_directory)
    sources = ' '
    for root, dirs, files in os.walk(source_directory):
        for f in files:
            if f.endswith('.java'):
                sources += os.path.join(root, f) + ' '
    command = 'javac ' + misc_compiler_args + ' -d ' + output_directory + sources
    if classpath:
        command += '-cp ' + classpath
    print(command)
    return os.system(command) == 0

def run(main_class, args):
    command = 'java -ea -cp ' + classpath \
        + output_directory + os.pathsep + ' ' + main_class + ' ' + args
    print(command)
    os.system(command)

if len(sys.argv) <= 1:
    if build():
        run(main_class, '')
else:
    for (i, task) in enumerate(sys.argv[1:]):
        if task == 'count':
            count();
        elif task == 'build':
            if not build():
                break
        elif task == 'run':
            args = ' '.join(sys.argv[i+2:])
            run(main_class, args)
            break
        elif task == 'test':
            args = ' '.join(sys.argv[i+2:])
            run(test_class, args)
            break
        else:
            print('Unrecognized task: ' + task)
            break
