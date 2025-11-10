package com.example.matchifyandroid.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.tooling.preview.Preview
import com.example.matchifyandroid.Controllers.MissionFormController
import com.example.matchifyandroid.ui.theme.MatchiFyAndroidTheme

@Composable
fun MissionFormScreen() {
    val controller = remember { MissionFormController() }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        // Title in top bar
        Text(
            text = "New Mission",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 12.dp)
        )

        // Main heading
        Text(
            text = "Publish a Mission",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // TITLE
        LabeledTextField(
            label = "Title",
            value = controller.title,
            onValueChange = { controller.title = it },
            placeholder = "e.g. Social Media Campaign for a Brand"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // DESCRIPTION
        LabeledTextField(
            label = "Description",
            value = controller.description,
            onValueChange = { controller.description = it },
            placeholder = "",
            maxLines = 6
        )

        Spacer(modifier = Modifier.height(16.dp))

        // DURATION
        LabeledTextField(
            label = "Duration",
            value = controller.duration,
            onValueChange = { controller.duration = it },
            placeholder = "e.g. 3 months, 2 weeks"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // BUDGET
        LabeledTextField(
            label = "Budget",
            value = controller.budget,
            onValueChange = { controller.budget = it },
            placeholder = "e.g. 1500 dt, 100 dt/day",
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(16.dp))

        // SKILLS
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Required Skills",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = controller.newSkill,
                    onValueChange = { controller.newSkill = it },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    placeholder = { Text("Add a skill") },
                    singleLine = true
                )

                IconButton(
                    onClick = { controller.addSkill() },
                    enabled = controller.skills.size < controller.maxSkills,
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            if (controller.skills.size < controller.maxSkills)
                                Color(0xFF2F80ED)
                            else
                                Color.Gray,
                            shape = RoundedCornerShape(50)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add skill",
                        tint = Color.White
                    )
                }
            }

            if (controller.skills.size >= controller.maxSkills) {
                Text(
                    text = "You can add up to ${controller.maxSkills} skills.",
                    fontSize = 12.sp,
                    color = Color.Red
                )
            }

            if (controller.skills.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                SkillChipsRow(
                    skills = controller.skills,
                    onRemove = { index -> controller.removeSkill(index) }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // BUTTON PUBLISH
        Button(
            onClick = { controller.submit() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F80ED))
        ) {
            Text(
                text = "Publish Mission",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

@Composable
fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,

    maxLines: Int = 1
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 0.dp)
                .wrapContentHeight(),
            placeholder = { Text(placeholder) },
            singleLine = maxLines == 1,
            maxLines = maxLines,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SkillChipsRow(
    skills: List<String>,
    onRemove: (Int) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        skills.forEachIndexed { index, skill ->
            Surface(
                shape = RoundedCornerShape(50),
                color = Color(0xFFE7E9F3)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = skill,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp)
                    )
                    IconButton(
                        onClick = { onRemove(index) },
                        modifier = Modifier.size(18.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Remove skill",
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

// 🔹 PREVIEW
@Preview(showBackground = true)
@Composable
fun MissionFormScreenPreview() {
    MatchiFyAndroidTheme {
        MissionFormScreen()
    }
}