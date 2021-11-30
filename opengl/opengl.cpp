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

	// > Buffers are data arrays in gpu memory
	// Fills a buffer with vertex positions and set its id in bufferID
	unsigned int bufferid;
	{
		glGenBuffers(1, &bufferid);
		glBindBuffer(GL_ARRAY_BUFFER, bufferid);
		float positions[] = {
			-0.5f, -0.5,
			 0.5f, -0.5f,
			 0.5f,  0.5f,
			-0.5f,  0.5f
		};
		glBufferData(GL_ARRAY_BUFFER, 4 * 2 * sizeof(float), positions, GL_STATIC_DRAW);
	}

	// Fills a buffer with triangle indexes and set its id in ibo
	unsigned int ibo;
	{
		glGenBuffers(1, &ibo);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		unsigned int indices[] = {
			0, 1, 2,
			2, 3, 0
		};
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, 2 * 3 * sizeof(unsigned int), indices, GL_STATIC_DRAW);
	}

	// Creates the triangle data array
	// vector<>

	// > Vertex attributes are attributes for buffer data that contain vertex informations, for the shader to know how to process them
	// Set vertex attributes
	glVertexAttribPointer(0, 2, GL_FLOAT, GL_FALSE, 2 * sizeof(float), 0);
	glEnableVertexAttribArray(0);

	// > Shaders are pieces of glsl gpu code that contain a vertex and a fragment shader.
	// > Vertex shaders contain data
	// > Fragment shaders compute colors for a given pixel on a shaded surface
	// Set the rendering shader
	unsigned int shader;
	{
		ShaderProgramSources sources = globalManager.ParseShader("shaders/test.glsl");
		shader = globalManager.CreateShader(sources.VertSource, sources.FragSource);
	}

	// Binds the buffers and shaders to draw elements
	{
		glBindBuffer(GL_ARRAY_BUFFER, bufferid);
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glUseProgram(shader);
	}

	/* Loop until the user closes the window */
	while (!glfwWindowShouldClose(window)) {
		/* Render here */
		glClear(GL_COLOR_BUFFER_BIT);

		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
		glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, nullptr);

		/* Swap front and back buffers */
		glfwSwapBuffers(window);

		/* Poll for and process events */
		glfwPollEvents();
	}

	glfwTerminate();
	return 0;
}
