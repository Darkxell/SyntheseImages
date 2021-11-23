#pragma once
#include <string>

struct ShaderProgramSources {
	std::string VertSource;
	std::string FragSource;
};

class ShaderManager {

public:

	unsigned int CompileShader(unsigned int type, const std::string& source);

	unsigned int CreateShader(const std::string& vertexShader, const std::string fragmentShader);

	ShaderProgramSources ParseShader(const std::string& file);

};

extern ShaderManager globalManager;
