/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package main;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import main.model.CommandSequence;
import main.model.CommandSequenceBuilder;
import main.model.youtube.keyboard.IYoutubeKeyboardTextCommandFactory;
import main.model.youtube.keyboard.YoutubeKeyboardTextCommandFactory;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class YoutubeKeyboardTextCommandFactoryTests {

    @Test
    public void YoutubeTextGenerationCommandTest() throws Exception {

    	// Arrange
    	CommandSequenceBuilder commandBuilder = CommandSequenceBuilder.CreateCommandSequence();
    	IYoutubeKeyboardTextCommandFactory youtubeKeyboardFactory = commandBuilder.getYoutubeKeyboardCommandManager();
    	String textToTest = "Un petit test";
    	String resultExpected = "{D-D-R-R-R-R-R-R-X-U-X-D-D-L-L-L-L-L-L-D-X-U-U-R-X-U-U-R-R-R-X-D-D-R-X-U-L-L-L-L-X-D-R-R-R-R-X-D-L-L-L-L-L-D-X-U-U-R-R-R-R-R-X-U-U-L-X-D-D-X-R-X}";
    	
    	// Action
    	youtubeKeyboardFactory.addCommandsFromText(commandBuilder, textToTest, 'A');
    	String sequenceResult = commandBuilder.build();
    	
    	// Asserts
    	assertEquals(resultExpected, sequenceResult);
    }
    
    

    @Test
    public void YoutubeTextGenerationCommand_SimpleTestOnBothKeyboardsTest() throws Exception {

    	// Arrange
    	CommandSequenceBuilder commandBuilder = CommandSequenceBuilder.CreateCommandSequence();
    	IYoutubeKeyboardTextCommandFactory youtubeKeyboardFactory = commandBuilder.getYoutubeKeyboardCommandManager();
    	String textToTest = "Le 92i";
    	String resultExpected = "{D-R-R-R-R-X-U-X-D-D-D-L-L-L-L-D-X-U-U-U-R-R-R-R-R-R-R-X-L-D-L-L-L-L-X-U-U-L-X-D-R-R-R-R-R-R-X-L-L-L-L-L-L-X}";
    	
    	// Action
    	youtubeKeyboardFactory.addCommandsFromText(commandBuilder, textToTest, 'A');
    	String sequenceResult = commandBuilder.build();
    	
    	// Asserts
    	assertEquals(resultExpected, sequenceResult);
    }
    
    

}
