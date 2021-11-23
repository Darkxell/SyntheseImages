#include <iostream>
#include <fstream>
#include <string>
#include <sstream>

#include <GL/glew.h>
#include <GLFW/glfw3.h>

#include "ShaderManager.h"

ShaderManager globalManager;

unsigned int ShaderManager::CompileShader(unsigned int type, const std::string& source) {
	
	// Shader compile
	unsigned int id = glCreateShader(type);
	const char* src = source.c_str();

	glShaderSource(id, 1, &src, nullptr);
	glCompileShader(id);

	//Error handling
	int result;
	glGetShaderiv(id, GL_COMPILE_STATUS, &result);
	if (!result) {
		int length;
		glGetShaderiv(id, GL_INFO_LOG_LENGTH, &length);
		char* message = (char*)alloca(length * sizeof(char));

		glGetShaderInfoLog(id, length, &length, message);
		std::cout << "Failed to compile shader : " << std::endl;
		std::cout << message << std::endl;

		glDeleteShader(id);
		return 0;
	}

	return id;
}

unsigned int ShaderManager::CreateShader(const std::string& vertexShader, const std::string fragmentShader) {
	unsigned int program = glCreateProgram();
	unsigned int vs = CompileShader(GL_VERTEX_SHADER, vertexShader);
	unsigned int fs = CompileShader(GL_FRAGMENT_SHADER, fragmentShader);

	glAttachShader(program, vs); glAttachShader(program, fs);

	glLinkProgram(program);
	glValidateProgram(program);

	glDeleteShader(vs); glDeleteShader(fs);

	return program;
}

ShaderProgramSources ShaderManager::ParseShader(const std::string& file) {
	std::ifstream input(file);

	enum class ShaderType {
		NONE = -1, VERTEX = 0, FRAGMENT = 1
	};
	ShaderType currenttype = ShaderType::NONE;

	std::string line;
	std::stringstream ss[2];
	bool hasreadline = false;
	while (getline(input, line)) {
		hasreadline = true;
		if (line.find("#shader") != std::string::npos) {
			if (line.find("vertex") != std::string::npos)
				currenttype = ShaderType::VERTEX;
			else if (line.find("fragment") != std::string::npos)
				currenttype = ShaderType::FRAGMENT;
			else
				currenttype = ShaderType::NONE;
		}
		else if (currenttype != ShaderType::NONE) {
			// Parse the line in the appropriate string buffer
			ss[(int)currenttype] << line << '\n';
		}
	}
	if (!hasreadline) {
		std::cout << "Could not read file at : " << file << std::endl;
	}
	return { ss[0].str(), ss[1].str() };
}
