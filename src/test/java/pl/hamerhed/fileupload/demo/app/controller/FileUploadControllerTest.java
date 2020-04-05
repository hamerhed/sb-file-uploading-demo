package pl.hamerhed.fileupload.demo.app.controller;

import static org.assertj.core.api.Assertions.assertThat;
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

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(FileUploadController.class)
public class FileUploadControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;

	@Before
	public void setup() {
	}

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

		mockMvc.perform(fileUpload("/updateformByModel/1").file(file).param("title", "asdf"))
				.andExpect(status().isOk());
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
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/updateFilesByModel/1").file(file)
				.file(file2).with(new RequestPostProcessor() {

					@Override
					public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
						request.setMethod("PUT");
						return request;
					}
				});

		mockMvc.perform(builder.param("title", "asdf")).andExpect(status().isOk());
	}

	private class FileMetadata {
		private String filename;

		private String key;

		private String title;

		private FileMetadata() {}
		
		public FileMetadata(String filename, String key, String title) {
			super();
			this.filename = filename;
			this.key = key;
			this.title = title;
		}

		public String getFilename() {
			return filename;
		}

		public void setFilename(String filename) {
			this.filename = filename;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
		
		
	}
	
	/**
	 * Test case checking the possiblity of passing multiple files with additional information related to each.
	 * It is rather workaround as spring boot seems not support such scenarios natively.
	 * The idea is to pass additional information as a RequestPart and provide there an 
	 * information enabling connection between the file and its metadata. Here the filename was used.
	 * Moreover it seems that Spring is able to preserve data order so the access by index
	 * can be used too.
	 * @throws Exception
	 */
	@Test
	public void testUpdateMultiFilesWithModelAttribute() throws Exception {
		MockMultipartFile file = new MockMultipartFile("files", "new_file.txt", "text/plain",
				"new file content".getBytes());
		MockMultipartFile file2 = new MockMultipartFile("files", "new_file2.txt", "text/plain",
				"new file2 content".getBytes());

		FileMetadata[] params = {new FileMetadata("new_file.txt", "model_1", "aaa"),
								 new FileMetadata("new_file2.txt", "model_2", "bbb")};
		
		MockMultipartFile metadata = new MockMultipartFile("metadata", "", "application/json", mapper.writeValueAsBytes(params));
		// as file multipart by default uses POST change it to PUT with postprocessing
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/updateMultiFiles/1").file(file)
				.file(file2)
				.file(metadata)
				.with(new RequestPostProcessor() {
					@Override
					public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
						request.setMethod("PUT");
						return request;
					}
				});

		String resp = mockMvc.perform(builder).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		
		FileUploadResult[] results = mapper.readValue(resp, FileUploadResult[].class);
		
		assertThat(results.length).isEqualTo(2);
		assertThat(results[0].getFilename()).isEqualTo(file.getOriginalFilename());
		assertThat(results[0].getFileLength()).isEqualTo(file.getSize());
		assertThat(results[0].getKey()).isEqualTo("model_1");
		
		assertThat(results[1].getFilename()).isEqualTo(file2.getOriginalFilename());
		assertThat(results[1].getFileLength()).isEqualTo(file2.getSize());
		assertThat(results[1].getKey()).isEqualTo("model_2");
	}
}
