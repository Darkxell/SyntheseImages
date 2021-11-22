#include <iostream>
#include <GL/glew.h>
#include <GLFW/glfw3.h>

int main(void)
{
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
		std::cout << "Opengl version : " << glGetString(GL_VERSION)  << std::endl;
	}
	
	/* Loop until the user closes the window */
	while (!glfwWindowShouldClose(window)) {
		/* Render here */
		glClear(GL_COLOR_BUFFER_BIT);

		glBegin(GL_TRIANGLES);
		glVertex2f(-0.5f, -0.5f);
		glVertex2f(0.0f, 0.5f);
		glVertex2f(0.5f, -0.5f);
		glEnd();

		/* Swap front and back buffers */
		glfwSwapBuffers(window);

		/* Poll for and process events */
		glfwPollEvents();
	}

	glfwTerminate();
	return 0;
}
