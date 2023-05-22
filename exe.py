import os
import shutil

def process_spring_factories():
    current_dir = os.getcwd()
    process_directories(current_dir)

def process_directories(dir_path):
    for root, dirs, files in os.walk(dir_path):
        for dir_name in dirs:
            if dir_name.endswith("boot-starter"):
                process_spring_factories_file(root, dir_name)

def process_spring_factories_file(dir_path, dir_name):
    spring_factories_path = os.path.join(dir_path, dir_name, "src/main/resources/META-INF/spring.factories")
    if os.path.exists(spring_factories_path):
        with open(spring_factories_path, "r") as file:
            lines = file.readlines()
        
        if lines:
            # 创建spring目录
            spring_dir = os.path.join(dir_path, "spring")
            os.makedirs(spring_dir, exist_ok=True)

            # 创建org.springframework.boot.autoconfigure.AutoConfiguration.imports文件
            target_file_path = os.path.join(spring_dir, "org.springframework.boot.autoconfigure.AutoConfiguration.imports")

            # 提取键值对右边的值，并写入目标文件
            with open(target_file_path, "w") as target_file:
                for line in lines:
                    if "=" in line:
                        _, value = line.split("=", 1)
                        target_file.write(value)
    
            print(f"Processed {spring_factories_path} and created {target_file_path}")

process_spring_factories()
