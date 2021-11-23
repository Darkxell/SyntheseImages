#include <iostream>
#include <fstream>
#include <string>
#include <sstream>

#include <GL/glew.h>
#include <GLFW/glfw3.h>

#include "ShaderManager.h"

/*struct Vertex {

};*/

int main(void) {
	// https://www.glfw.org/
	GLFWwindow* window;

	/* Initialize the libraries and creates an openGL context.*/
	{
		if (!glfwInit())
			return -1;

		/* Create a windowed mode window and its OpenGL context */
		window = glfwCreateWindow(640, 480, "OpenGL", NULL, NULL);
		if (!window) {
			glfwTerminate();
			return -1;
		}

		/* Make the window's context current */
		glfwMakeContextCurrent(window);

		if (glewInit() != GLEW_OK) {
			std::cout << "Glew couldn't initialize." << std::endl;
			return -1;
		}

		std::cout << "Context ready, with GLEW and GLFW initialised." << std::endl;
		std::cout << "Opengl version : " << glGetString(GL_VERSION) << std::endl;
	}

	// Generates a buffer of id "bufferid"
	unsigned int bufferid;
	glGenBuffers(1, &bufferid);
	// Bind to the memory buffer
	glBindBuffer(GL_ARRAY_BUFFER, bufferid);

	// Creates the triangle data array
	//vector<>

	// Fills the buffer with triangle positions
	float positions[] = {
		-0.5f, -0.5,
		0.5f, -0.5f,
		0.5f, 0.5f,

		0.5f, 0.5,
		-0.5f, 0.5f,
		-0.5f, -0.5f
	};
	glBufferData(GL_ARRAY_BUFFER, 6 * 2 * sizeof(float), positions, GL_STATIC_DRAW);

	// Set vertex attributes
	glVertexAttribPointer(0, 2, GL_FLOAT, GL_FALSE, 2 * sizeof(float), 0);
	glEnableVertexAttribArray(0);

	// Set the rendering shader
	ShaderProgramSources sources = globalManager.ParseShader("shaders/test.glsl");
	unsigned int shader = globalManager.CreateShader(sources.VertSource, sources.FragSource);
	glUseProgram(shader);

	/* Loop until the user closes the window */
	while (!glfwWindowShouldClose(window)) {
		/* Render here */
		glClear(GL_COLOR_BUFFER_BIT);

		glDrawArrays(GL_TRIANGLES, 0, 6);

		/* Swap front and back buffers */
		glfwSwapBuffers(window);

		/* Poll for and process events */
		glfwPollEvents();
	}

	glfwTerminate();
	return 0;
}
