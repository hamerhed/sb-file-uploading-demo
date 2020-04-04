package pl.hamerhed.fileupload.demo.app.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

@RunWith(SpringRunner.class)
@WebMvcTest(FileUploadController.class)
public class FileUploadControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void setup() {}

	@Test
	public void keepalive() throws Exception {
		mockMvc.perform(get("/test")).andExpect(status().isOk()).andExpect(content().string("ok"));
	}

	@Test
	public void testPostFileUpload() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "file content".getBytes());

		mockMvc.perform(fileUpload("/upload").file(file)).andExpect(status().isOk());
	}

	@Test
	public void testPostFileWithOtherDataUpload() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "file.txt", "text/plain", "file content".getBytes());

		mockMvc.perform(fileUpload("/uploadform").file(file).param("title", "mytitle")).andExpect(status().isOk());
	}

	@Test
	public void testPutFileWithOtherData() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "new_file.txt", "text/plain",
				"new file content".getBytes());

		// as file multipart by default uses POST change it to PUT
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/updateform/1").file(file)
				.with(new RequestPostProcessor() {

					@Override
					public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
						request.setMethod("PUT");
						return request;
					}
				});

		mockMvc.perform(builder.param("title", "new title")).andExpect(status().isOk());
	}

	@Test
	public void testPutFileWithNoData() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "new_file.txt", "text/plain",
				"new file content".getBytes());

		// as file multipart by default uses POST change it to PUT with postprocessing
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/updateform/1").file(file)
				.with(new RequestPostProcessor() {

					@Override
					public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
						request.setMethod("PUT");
						return request;
					}
				});

		mockMvc.perform(builder).andExpect(status().isBadRequest()).andExpect(jsonPath("$.msg", is("bind exception")))
				.andExpect(jsonPath("$.code", is("404")));
	}
	
	@Test
	public void testPostFileByModelAttribute() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "new_file.txt", "text/plain",
				"new file content".getBytes());

		// as file multipart by default uses POST change it to PUT with postprocessing
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/updateformByModel/1").file(file)
				.with(new RequestPostProcessor() {

					@Override
					public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
						request.setMethod("PUT");
						return request;
					}
				});

		mockMvc.perform(fileUpload("/updateformByModel/1").file(file).param("title", "asdf")).andExpect(status().isOk());
	}
	
	@Test
	public void testPutFileByModelAttribute() throws Exception {
		MockMultipartFile file = new MockMultipartFile("file", "new_file.txt", "text/plain",
				"new file content".getBytes());

		// as file multipart by default uses POST change it to PUT with postprocessing
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/updateformByModel/1").file(file)
				.with(new RequestPostProcessor() {

					@Override
					public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
						request.setMethod("PUT");
						return request;
					}
				});

		mockMvc.perform(builder.param("title", "asdf")).andExpect(status().isOk());
	}
	
	@Test
	public void testPutFilesByModelAttribute() throws Exception {
		MockMultipartFile file = new MockMultipartFile("files", "new_file.txt", "text/plain",
				"new file content".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("files", "new_file2.txt", "text/plain",
				"new file2 content".getBytes());

		// as file multipart by default uses POST change it to PUT with postprocessing
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/updateFilesByModel/1").file(file).file(file2)
				.with(new RequestPostProcessor() {

					@Override
					public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
						request.setMethod("PUT");
						return request;
					}
				});

		mockMvc.perform(builder.param("title", "asdf")).andExpect(status().isOk());
	}
	
	
}
