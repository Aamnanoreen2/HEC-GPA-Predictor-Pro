package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.*
import com.example.ui.viewmodel.AcademicViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApplicationScreen(viewModel: AcademicViewModel) {
    var tab by remember { mutableStateOf(0) }

    val universities by viewModel.availableUniversities.collectAsState()
    val activeUniversity by viewModel.selectedUniversity.collectAsState()

    Scaffold(
        topBar = {
            var expanded by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(64.dp)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Brand Logo & Titles
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable { expanded = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "School Logo",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "PREDICTOR PRO",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "HEC GPA Central",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.offset(y = (-2).dp)
                        )
                    }
                }

                // Right Profile Icon with Dropdown Triggers
                Box {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE2E8F0), CircleShape)
                            .clickable { expanded = true }
                            .testTag("univ_selector_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "AH",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("University Schemes & Scaling", fontWeight = FontWeight.Bold) },
                            onClick = {},
                            enabled = false
                        )
                        HorizontalDivider()
                        universities.forEach { uni ->
                            DropdownMenuItem(
                                text = {
                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        if (uni.universityName == activeUniversity) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Active",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        Text(uni.universityName)
                                    }
                                },
                                onClick = {
                                    viewModel.selectUniversity(uni.universityName)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                windowInsets = WindowInsets.navigationBars,
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                val navColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = Color(0xFFD0E1FF),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )

                NavigationBarItem(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    icon = { Icon(if (tab == 0) Icons.Default.Dashboard else Icons.Outlined.Dashboard, contentDescription = "Dashboard") },
                    label = { Text("Home", fontSize = 11.sp, fontWeight = if (tab == 0) FontWeight.Bold else FontWeight.Normal) },
                    colors = navColors,
                    modifier = Modifier.testTag("nav_home")
                )
                NavigationBarItem(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    icon = { Icon(if (tab == 1) Icons.Default.AddChart else Icons.Outlined.AddChart, contentDescription = "GPA Prediction") },
                    label = { Text("Predictor", fontSize = 11.sp, fontWeight = if (tab == 1) FontWeight.Bold else FontWeight.Normal) },
                    colors = navColors,
                    modifier = Modifier.testTag("nav_predictor")
                )
                NavigationBarItem(
                    selected = tab == 2,
                    onClick = { tab = 2 },
                    icon = { Icon(if (tab == 2) Icons.Default.TrackChanges else Icons.Outlined.TrackChanges, contentDescription = "CGPA Target") },
                    label = { Text("Planner", fontSize = 11.sp, fontWeight = if (tab == 2) FontWeight.Bold else FontWeight.Normal) },
                    colors = navColors,
                    modifier = Modifier.testTag("nav_planner")
                )
                NavigationBarItem(
                    selected = tab == 3,
                    onClick = { tab = 3 },
                    icon = { Icon(if (tab == 3) Icons.Default.Hub else Icons.Outlined.Hub, contentDescription = "Scales") },
                    label = { Text("Settings", fontSize = 11.sp, fontWeight = if (tab == 3) FontWeight.Bold else FontWeight.Normal) },
                    colors = navColors,
                    modifier = Modifier.testTag("nav_scales")
                )
                NavigationBarItem(
                    selected = tab == 4,
                    onClick = { tab = 4 },
                    icon = { Icon(if (tab == 4) Icons.Default.Psychology else Icons.Outlined.Psychology, contentDescription = "AI Advisor") },
                    label = { Text("AI Advisor", fontSize = 11.sp, fontWeight = if (tab == 4) FontWeight.Bold else FontWeight.Normal) },
                    colors = navColors,
                    modifier = Modifier.testTag("nav_ai")
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (tab) {
                0 -> DashboardTab(viewModel)
                1 -> GpaPredictorTab(viewModel)
                2 -> TargetCGPAPlannerTab(viewModel)
                3 -> ConfigurationTemplatesTab(viewModel)
                4 -> AIAdvisorTab(viewModel)
            }
        }
    }
}

// --- TAB 1: DASHBOARD ---
@Composable
fun QuickStatsHeroCard(stats: AcademicViewModel.AcademicStats, totalGoalCredits: Int) {
    val progressFraction = if (totalGoalCredits > 0) stats.completedCredits.toFloat() / totalGoalCredits else 0.0f
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("quick_stats_hero_card"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Accent background canvas decoration
            Canvas(
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 30.dp, y = (-30).dp)
            ) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.05f),
                    radius = size.minDimension / 2
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = "CURRENT CGPA",
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = "%.2f".format(stats.completedCgpa),
                                fontSize = 44.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = " / 4.0",
                                fontSize = 18.sp,
                                color = Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }
                    }
                    
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .padding(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Trend Icon",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Degree Completion",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "${"%.0f".format(progressFraction * 100)}%",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progressFraction.coerceIn(0f, 1f))
                                .background(Color.White, CircleShape)
                        )
                    }

                    Text(
                        text = "*${stats.completedCredits} / $totalGoalCredits Credit Hours Completed",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 10.sp,
                        style = androidx.compose.ui.text.TextStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardTab(viewModel: AcademicViewModel) {
    val stats = viewModel.calculateAcademicStats()
    val goal by viewModel.academicGoal.collectAsState()
    val activeUniversity by viewModel.selectedUniversity.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Quick Stats Layout matching "Sleek Interface"
        item {
            val totalGoalCredits = goal?.totalCreditsRequired ?: 130
            QuickStatsHeroCard(stats = stats, totalGoalCredits = totalGoalCredits)
        }

        // Active environment configuration preset information card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "UNIVERSITY PRESET",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "HEC ACTIVE",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = activeUniversity,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9))
                    Text(
                        text = "Using Pakistani standard HEC 4.0 relative scaling metrics with offline database storage.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        // Predicted Outcomes Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = "Trend Icon",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Forecasted Future CGPA",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Predicted: ${"%.2f".format(stats.predictedCgpa)} CGPA",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Calculated from completed and active future semester plans combined.",
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // Canvas drawn GPA trend graph
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "GPA Progression Trend Chart",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    val semestersList by viewModel.semesters.collectAsState()
                    val subjectsList by viewModel.allSubjects.collectAsState()

                    // Compute points for the graph
                    val points = semestersList.map { sem ->
                        val subInSem = subjectsList.filter { it.semesterId == sem.id }
                        val credits = subInSem.sumOf { it.creditHours }
                        val weightedPoints = subInSem.sumOf { it.creditHours * it.gpa }
                        if (credits > 0) weightedPoints / credits else 0.0
                    }.filter { it > 0.0 }

                    if (points.size >= 2) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .padding(8.dp)
                        ) {
                            val canvasWidth = size.width
                            val canvasHeight = size.height

                            val maxLineGpa = 4.0
                            val minLineGpa = 0.0

                            val xInterval = canvasWidth / (points.size - 1)
                            val path = Path()

                            // Draw lines
                            points.forEachIndexed { index, gpa ->
                                val x = index * xInterval
                                val y = canvasHeight - ((gpa - minLineGpa) / (maxLineGpa - minLineGpa) * canvasHeight).toFloat()

                                if (index == 0) {
                                    path.moveTo(x, y)
                                } else {
                                    path.lineTo(x, y)
                                }
                            }

                            // Draw gradient color below line
                            val fillPath = Path().apply {
                                addPath(path)
                                lineTo((points.size - 1) * xInterval, canvasHeight)
                                lineTo(0f, canvasHeight)
                                close()
                            }

                             drawPath(
                                fillPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF005AC1).copy(alpha = 0.25f),
                                        Color.Transparent
                                    )
                                )
                            )

                            drawPath(
                                path,
                                color = Color(0xFF005AC1),
                                style = Stroke(width = 6f)
                            )

                            // Draw node circles
                            points.forEachIndexed { index, gpa ->
                                val x = index * xInterval
                                val y = canvasHeight - ((gpa - minLineGpa) / (maxLineGpa - minLineGpa) * canvasHeight).toFloat()
                                drawCircle(
                                    color = Color(0xFFD0E1FF),
                                    radius = 10f,
                                    center = Offset(x, y)
                                )
                                drawCircle(
                                    color = Color(0xFF005AC1),
                                    radius = 6f,
                                    center = Offset(x, y)
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("First recorded", fontSize = 10.sp, color = Color.Gray)
                            Text("Latest semester", fontSize = 10.sp, color = Color.Gray)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Enter grades in at least 2 semesters to show live GPA progression graphs.",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Analytical Overview Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Academic Metric Insights",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Best Semester", fontSize = 10.sp, color = Color.Gray)
                                Text(
                                    text = stats.highestGpaSemester.first,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    maxLines = 1
                                )
                                Text(
                                    text = if (stats.highestGpaSemester.second > 0) "%.2f GPA".format(stats.highestGpaSemester.second) else "N/A",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("Growth Factor", fontSize = 10.sp, color = Color.Gray)
                                Text(
                                    text = if (stats.academicGrowth >= 0) "Ascending progression" else "Decending curve",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${"%.1f".format(stats.academicGrowth)}%",
                                    fontWeight = FontWeight.Bold,
                                    color = if (stats.academicGrowth >= 0) Color(0xFF00AA50) else Color.Red,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Scholarship eligibility alerts
        item {
            val meritsAlertStr = if (stats.completedCgpa >= 3.75) {
                "Eligible for Presidential Gold Merit & Dean's Honor Roll placement!"
            } else if (stats.completedCgpa >= 3.50) {
                "Eligible for High Academic Honors and Dean's List nomination."
            } else if (stats.completedCgpa >= 3.00) {
                "Meets general Pakistan Merit scholarship thresholds. Keep up the consistency!"
            } else {
                "GPA is below academic scholarship requirements (usually 3.0). Visit AI advisor to plan recovery paths."
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (stats.completedCgpa >= 3.0) Color(0xFFE8F6EE) else Color(0xFFFFF2F2)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (stats.completedCgpa >= 3.0) Icons.Default.MilitaryTech else Icons.Default.Warning,
                        contentDescription = "Scholarship Badge",
                        tint = if (stats.completedCgpa >= 3.0) Color(0xFF006A6A) else Color.Red
                    )
                    Column {
                        Text(
                            text = "Honor Roll status",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = if (stats.completedCgpa >= 3.0) Color(0xFF006A6A) else Color.Red
                        )
                        Text(
                            text = meritsAlertStr,
                            fontSize = 11.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}


// --- TAB 2: GPA & CGPA PREDICTOR ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GpaPredictorTab(viewModel: AcademicViewModel) {
    val semestersList by viewModel.semesters.collectAsState()
    val subjectsList by viewModel.allSubjects.collectAsState()
    val gradeMappingsList by viewModel.gradeMappings.collectAsState()

    var showSemesterDialog by remember { mutableStateOf(false) }
    var semesterNameInput by remember { mutableStateOf("") }
    var semesterCompletedChecked by remember { mutableStateOf(false) }

    var activeSubsemesterIdForDialog by remember { mutableStateOf<Int?>(null) }
    var subjectNameInput by remember { mutableStateOf("") }
    var courseCreditsInput by remember { mutableStateOf("3") }
    var gradeDropdownSelected by remember { mutableStateOf("A") }
    var gradeDropdownExpanded by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Quick Title Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Semester Course Planner",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Forecast semesters expected GPA and see live predictive CGPA.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                Button(
                    onClick = { showSemesterDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("add_semester_action")
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add", fontSize = 12.sp)
                }
            }
        }

        // Empty State Handle
        if (semestersList.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Source,
                            contentDescription = "Empty",
                            modifier = Modifier.size(60.dp),
                            tint = Color.Gray.copy(alpha = 0.5f)
                        )
                        Text(
                            text = "No recorded semesters yet.",
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                        Text(
                            text = "Click 'Add' above to start organizing and planning courses.",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // Loop semesters and show detailed interactive subject cards
        items(semestersList) { sem ->
            val subjectsInSem = subjectsList.filter { it.semesterId == sem.id }
            val totalSemCredits = subjectsInSem.sumOf { it.creditHours }
            val totalSemPoints = subjectsInSem.sumOf { it.creditHours * it.gpa }
            val calculatedSemGpa = if (totalSemCredits > 0) totalSemPoints / totalSemCredits else 0.0

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("semester_card_${sem.id}"),
                colors = CardDefaults.cardColors(
                    containerColor = if (sem.isCompleted) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(
                    1.dp,
                    if (sem.isCompleted) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else Color.LightGray.copy(alpha = 0.5f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Semester Header Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = sem.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (sem.isCompleted) Color(0xFFE2F0D9) else Color(0xFFFFF2CC)
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = if (sem.isCompleted) "Completed" else "Planned / Dynamic",
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontWeight = FontWeight.Bold,
                                        color = if (sem.isCompleted) Color(0xFF385723) else Color(0xFF7F6000)
                                    )
                                }
                            }
                            Text(
                                text = "GPA calculated: ${"%.2f".format(calculatedSemGpa)} | Creds: $totalSemCredits",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        Row {
                            IconButton(onClick = { activeSubsemesterIdForDialog = sem.id }) {
                                Icon(Icons.Default.AddCircleOutline, contentDescription = "Add course", tint = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(onClick = { viewModel.deleteSemester(sem.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.7f))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Subjects list inside this semester
                    if (subjectsInSem.isEmpty()) {
                        Text(
                            text = "No added courses yet. Touch '+' icon inside header to add.",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            subjectsInSem.forEach { sub ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(sub.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                        Text("${sub.creditHours} Creds | Single Grade Value: ${sub.grade} (Gpa point: ${sub.gpa})", fontSize = 10.sp, color = Color.Gray)
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(sub.grade, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        }

                                        IconButton(
                                            onClick = { viewModel.deleteSubject(sub.id) },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(Icons.Default.Close, contentDescription = "Delete course", tint = Color.Red, modifier = Modifier.size(16.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal dialog to add Semester
    if (showSemesterDialog) {
        AlertDialog(
            onDismissRequest = { showSemesterDialog = false },
            title = { Text("Add Semester") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = semesterNameInput,
                        onValueChange = { semesterNameInput = it },
                        label = { Text("Semester Name") },
                        placeholder = { Text("e.g. Fall 2026") },
                        modifier = Modifier.fillMaxWidth().testTag("add_semester_name_input")
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = semesterCompletedChecked,
                            onCheckedChange = { semesterCompletedChecked = it },
                            modifier = Modifier.testTag("add_semester_completed_checkbox")
                        )
                        Text("Active Completed Semester?")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val nameStr = semesterNameInput.ifBlank { "Semester ${semestersList.size + 1}" }
                        viewModel.addSemester(nameStr, semesterCompletedChecked)
                        semesterNameInput = ""
                        semesterCompletedChecked = false
                        showSemesterDialog = false
                    },
                    modifier = Modifier.testTag("save_semester_confirm")
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSemesterDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Modal dialog to add Subject to active semesterId
    if (activeSubsemesterIdForDialog != null) {
        val targetSemId = activeSubsemesterIdForDialog!!
        AlertDialog(
            onDismissRequest = { activeSubsemesterIdForDialog = null },
            title = { Text("Add Course Record") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = subjectNameInput,
                        onValueChange = { subjectNameInput = it },
                        label = { Text("Course Name") },
                        placeholder = { Text("e.g. Calculus") },
                        modifier = Modifier.fillMaxWidth().testTag("add_course_name_input")
                    )

                    OutlinedTextField(
                        value = courseCreditsInput,
                        onValueChange = { courseCreditsInput = it },
                        label = { Text("Credit Hours") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("add_course_credits_input")
                    )

                    // Grade dropdown list selector
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedButton(
                            onClick = { gradeDropdownExpanded = true },
                            modifier = Modifier.fillMaxWidth().testTag("add_course_grade_dropdown_button")
                        ) {
                            Text("Assigned Grade: $gradeDropdownSelected")
                        }
                        DropdownMenu(
                            expanded = gradeDropdownExpanded,
                            onDismissRequest = { gradeDropdownExpanded = false }
                        ) {
                            gradeMappingsList.forEach { mapping ->
                                DropdownMenuItem(
                                    text = { Text("${mapping.grade} (${mapping.gpa} points)") },
                                    onClick = {
                                        gradeDropdownSelected = mapping.grade
                                        gradeDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val subName = subjectNameInput.ifBlank { "New Course" }
                        val creditsNum = courseCreditsInput.toIntOrNull() ?: 3
                        viewModel.addSubject(targetSemId, subName, creditsNum, gradeDropdownSelected)
                        subjectNameInput = ""
                        courseCreditsInput = "3"
                        activeSubsemesterIdForDialog = null
                    },
                    modifier = Modifier.testTag("save_course_confirm")
                ) {
                    Text("Save Course")
                }
            },
            dismissButton = {
                TextButton(onClick = { activeSubsemesterIdForDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}


// --- TAB 3: CGPA TARGET PLANNER & SEMESTER COURSE PLANNER ---
@Composable
fun TargetCGPAPlannerTab(viewModel: AcademicViewModel) {
    var desiredCgpaInput by remember { mutableStateOf("3.5") }
    var remainingCreditsInput by remember { mutableStateOf("50") }
    var totalDegreeCreditsInput by remember { mutableStateOf("130") }

    val stats = viewModel.calculateAcademicStats()
    val goal by viewModel.academicGoal.collectAsState()
    val semestersList by viewModel.semesters.collectAsState()
    val subjectsList by viewModel.allSubjects.collectAsState()
    val gradeMappings by viewModel.gradeMappings.collectAsState()

    // Dual Planner Tabs State: 0: Long-Term CGPA Matcher, 1: Upcoming Course Planner
    var plannerTab by remember { mutableStateOf(1) }

    // Dialog state
    var showAddCourseDialog by remember { mutableStateOf(false) }
    var editingSubject by remember { mutableStateOf<Subject?>(null) }

    var courseNameInput by remember { mutableStateOf("") }
    var courseCreditsInput by remember { mutableStateOf("3") }
    var selectedGradeInput by remember { mutableStateOf("A") }
    var gradeDropdownExpanded by remember { mutableStateOf(false) }

    var selectedPlannedSemId by remember { mutableStateOf<Int?>(null) }
    var showSemesterDeleteConfirmation by remember { mutableStateOf<Int?>(null) }

    val plannedSemesters = semestersList.filter { !it.isCompleted }

    val availableGrades = if (gradeMappings.isNotEmpty()) {
        gradeMappings.map { it.grade }
    } else {
        listOf("A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D", "F")
    }

    // Automatically pick the first planned semester if none is selected
    LaunchedEffect(plannedSemesters) {
        if (selectedPlannedSemId == null || plannedSemesters.none { it.id == selectedPlannedSemId }) {
            if (plannedSemesters.isNotEmpty()) {
                selectedPlannedSemId = plannedSemesters.first().id
            }
        }
    }

    LaunchedEffect(goal) {
        goal?.let {
            desiredCgpaInput = it.targetCgpa.toString()
            totalDegreeCreditsInput = it.totalCreditsRequired.toString()
            val remaining = kotlin.math.max(0, it.totalCreditsRequired - stats.completedCredits)
            remainingCreditsInput = remaining.toString()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Academic Planning Suite",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Simulate course grades, select credit options, and establish precise target goal tracking metrics.",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }

        // Sleek Dual Tab Selector Component
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                    .padding(4.dp)
            ) {
                listOf("Long-Term Goals", "Upcoming Term Planner").forEachIndexed { index, label ->
                    val isSelected = plannerTab == index
                    val containerBg = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                    val textCol = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(containerBg)
                            .clickable { plannerTab = index }
                            .padding(vertical = 10.dp)
                            .testTag("planner_tab_$index"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = textCol
                        )
                    }
                }
            }
        }

        // ================= SECTION 0: LONG-TERM CGPA MATCHER =================
        if (plannerTab == 0) {
            // Configuration Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("goals_configuration_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text("Planner Inputs", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

                        OutlinedTextField(
                            value = desiredCgpaInput,
                            onValueChange = { desiredCgpaInput = it },
                            label = { Text("Desired Final Graduation CGPA") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth().testTag("planner_desired_cgpa_input")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = remainingCreditsInput,
                                onValueChange = { remainingCreditsInput = it },
                                label = { Text("Remaining Credits") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f).testTag("planner_remaining_credits_input")
                            )

                            OutlinedTextField(
                                value = totalDegreeCreditsInput,
                                onValueChange = { totalDegreeCreditsInput = it },
                                label = { Text("Total Degree Credits") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f).testTag("planner_total_credits_input")
                            )
                        }

                        Button(
                            onClick = {
                                val targetVal = desiredCgpaInput.toDoubleOrNull() ?: 3.5
                                val totalVal = totalDegreeCreditsInput.toIntOrNull() ?: 130
                                viewModel.saveAcademicGoal(targetVal, totalVal)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("save_planner_configuration"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Calculate & Sync Goals")
                        }
                    }
                }
            }

            // Live Calculated Prediction Outputs
            item {
                val targetVal = desiredCgpaInput.toDoubleOrNull() ?: 3.5
                val remainingVal = remainingCreditsInput.toIntOrNull() ?: 50

                val currentFinishedCredits = stats.completedCredits
                val currentCompletedCgpa = stats.completedCgpa

                val totalExpectedCredits = currentFinishedCredits + remainingVal

                // Target calculation formula:
                // (TargetCGPA * TotalCredits - CurrentCGPA * FinishedCredits) / RemainingCredits
                val requiredGpa = if (remainingVal > 0) {
                    ((targetVal * totalExpectedCredits) - (currentCompletedCgpa * currentFinishedCredits)) / remainingVal
                } else {
                    0.0
                }

                val statusText: String
                val statusColor: Color
                val isFeasible: Boolean

                if (requiredGpa > 4.0) {
                    statusText = "UNACHIEVABLE. You would need a static GPA of ${"%.2f".format(requiredGpa)} which exceeds standard limits. Re-adjust your objectives or plan additional courses."
                    statusColor = Color.Red
                    isFeasible = false
                } else if (requiredGpa < 0.0) {
                    statusText = "ACHIEVED already! Even if you score 0.0, you retain your desired target. Fantastic work!"
                    statusColor = Color(0xFF00AA50)
                    isFeasible = true
                } else if (requiredGpa > 3.7) {
                    statusText = "HIGHLY AMBITIOUS! To reach this, you must secure an average GPA of ${"%.2f".format(requiredGpa)} (near-perfect A/A- grades) across all upcoming semesters."
                    statusColor = Color(0xFFFD7E14)
                    isFeasible = true
                } else {
                    statusText = "FEASIBLE. You require an average GPA of ${"%.2f".format(requiredGpa)} in remaining course hours. It is well within an achievable, realistic range."
                    statusColor = Color(0xFF005AC1)
                    isFeasible = true
                }

                Card(
                    modifier = Modifier.fillMaxWidth().testTag("forecast_roadmap_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Calculated Forecast Roadmap", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Average GPA Required next:", fontSize = 13.sp, color = Color.Gray)
                            Text(
                                text = if (requiredGpa > 0.0) "%.3f GPA".format(requiredGpa) else "0.00 GPA",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = statusColor
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = statusText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }
                }
            }

            // Simulated Best and Worst Case Scenarios
            item {
                val remainingVal = remainingCreditsInput.toIntOrNull() ?: 50
                val currentFinishedCredits = stats.completedCredits
                val currentCompletedCgpa = stats.completedCgpa
                val totalHours = currentFinishedCredits + remainingVal

                val bestCaseCgpa = if (totalHours > 0) {
                    ((currentCompletedCgpa * currentFinishedCredits) + (4.0 * remainingVal)) / totalHours
                } else {
                    4.0
                }

                val worstCaseCgpa = if (totalHours > 0) {
                    ((currentCompletedCgpa * currentFinishedCredits) + (1.5 * remainingVal)) / totalHours // Assuming minimum C- passing margin
                } else {
                    1.5
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Best-Case & Worst-Case Simulations", fontWeight = FontWeight.Bold)

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE2F0D9))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Icon(Icons.Default.Star, contentDescription = "Best Case", tint = Color(0xFF385723))
                                    Text("Best Case (All A)", fontSize = 10.sp, color = Color.DarkGray)
                                    Text(
                                        text = "%.2f CGPA".format(bestCaseCgpa),
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF385723),
                                        fontSize = 15.sp
                                    )
                                }
                            }

                            Card(
                                modifier = Modifier.weight(1f),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFFCE4D6))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Icon(Icons.Default.ArrowDownward, contentDescription = "Worst Case", tint = Color(0xFFC65911))
                                    Text("Worst Case (All C-)", fontSize = 10.sp, color = Color.DarkGray)
                                    Text(
                                        text = "%.2f CGPA".format(worstCaseCgpa),
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFC65911),
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // ================= SECTION 1: UPCOMING TERM PLANNER =================
        if (plannerTab == 1) {
            if (plannedSemesters.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("empty_planned_semesters_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.School,
                                    contentDescription = "New Plan",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Semester Sandbox Planner",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "List upcoming courses, assign expected credits, choose target grades, and predict the exact overall cumulative CGPA impact.",
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                            }

                            Button(
                                onClick = {
                                    viewModel.addSemester("Upcoming Term Plan", isCompleted = false)
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("init_planned_semester_button")
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Add")
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Create Upcoming Semester Plan")
                            }
                        }
                    }
                }
            } else {
                // Header Selector Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("planned_semesters_selector_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Upcoming Plan Selection:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Button(
                                    onClick = {
                                        viewModel.addSemester("Upcoming Term ${plannedSemesters.size + 1}", isCompleted = false)
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                    modifier = Modifier.height(36.dp).testTag("add_planned_term_btn"),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add Term", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("New Term", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            // Dynamic Selector Chips for different Term plans
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                plannedSemesters.forEach { sem ->
                                    val isSelected = selectedPlannedSemId == sem.id
                                    val containerCol = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                    val borderCol = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                                    val contentCol = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(containerCol)
                                            .border(1.dp, borderCol, RoundedCornerShape(8.dp))
                                            .clickable { selectedPlannedSemId = sem.id }
                                            .padding(horizontal = 12.dp, vertical = 8.dp)
                                            .testTag("planned_term_chip_${sem.id}"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = sem.name,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = contentCol
                                            )
                                            IconButton(
                                                onClick = { showSemesterDeleteConfirmation = sem.id },
                                                modifier = Modifier.size(16.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Delete",
                                                    tint = Color.Red.copy(alpha = 0.7f),
                                                    modifier = Modifier.size(12.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Analytics live impact report card matching current selection
                item {
                    val activeId = selectedPlannedSemId
                    val subjectsInSem = if (activeId != null) subjectsList.filter { it.semesterId == activeId } else emptyList()
                    val activeSemCredits = subjectsInSem.sumOf { it.creditHours }
                    val activeSemPoints = subjectsInSem.sumOf { it.creditHours * it.gpa }
                    val activeSemGpa = if (activeSemCredits > 0) activeSemPoints / activeSemCredits else 0.0

                    val totalCompletedCredits = stats.completedCredits
                    val totalCompletedPoints = stats.completedCgpa * stats.completedCredits
                    val expectedTotalCredits = totalCompletedCredits + activeSemCredits
                    val expectedTotalPoints = totalCompletedPoints + activeSemPoints
                    val expectedNewCgpa = if (expectedTotalCredits > 0) expectedTotalPoints / expectedTotalCredits else 0.0
                    val cgpaDelta = expectedNewCgpa - stats.completedCgpa

                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("live_analytics_forecast_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                    Text(
                                        text = "FORECASTING ENGINE",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        letterSpacing = 1.1.sp
                                    )
                                    Text(
                                        text = "Estimated CGPA Impact",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("EST. CGPA", fontSize = 8.sp, color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Bold)
                                        Text("%.2f".format(expectedNewCgpa), fontSize = 15.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            HorizontalDivider(color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Planned Term GPA card
                                Card(
                                    modifier = Modifier.weight(1f),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text("Planned Term GPA", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "%.2f".format(activeSemGpa),
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text("$activeSemCredits Class Credits", fontSize = 9.sp, color = Color.Gray)
                                    }
                                }

                                // Cumulative Evolution card
                                Card(
                                    modifier = Modifier.weight(1f),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text("Cumulative shift", fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Text(
                                                text = "%.2f".format(stats.completedCgpa),
                                                fontSize = 13.sp,
                                                color = Color.Gray,
                                                style = androidx.compose.ui.text.TextStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                                            )
                                            Icon(
                                                imageVector = Icons.Default.TrendingUp,
                                                contentDescription = "to",
                                                modifier = Modifier.size(12.dp),
                                                tint = Color.Gray
                                            )
                                            Text("%.2f".format(expectedNewCgpa), fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))

                                        // Delta badge
                                        val sign = if (cgpaDelta > 0) "+" else ""
                                        val signCol = if (cgpaDelta > 0.001) Color(0xFF2E7D32) else if (cgpaDelta < -0.001) Color(0xFFC62828) else Color.Gray
                                        Box(
                                            modifier = Modifier
                                                .background(signCol.copy(alpha = 0.1f), CircleShape)
                                                .border(1.dp, signCol.copy(alpha = 0.2f), CircleShape)
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "$sign%.2f GPA".format(cgpaDelta),
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = signCol
                                            )
                                        }
                                    }
                                }
                            }

                            // Dynamic explanatory review statement
                            val narrative = when {
                                cgpaDelta > 0.005 -> "Securing these expected grades will successfully elevate your overall cumulative CGPA by **%.3f** points.".format(cgpaDelta)
                                cgpaDelta < -0.005 -> "Careful! These predicted marks would decrease your cumulative CGPA by **%.3f** points. Aim for higher grade markers to prevent regression!".format(kotlin.math.abs(cgpaDelta))
                                else -> "Stability threshold. Your overall GPA will remain stable around **%.2f** with this course layout.".format(expectedNewCgpa)
                            }

                            Text(
                                text = narrative,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                                style = androidx.compose.ui.text.TextStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                            )
                        }
                    }
                }

                // Core course list item records section
                item {
                    val activeId = selectedPlannedSemId
                    val subjectsInSem = if (activeId != null) subjectsList.filter { it.semesterId == activeId } else emptyList()

                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("upcoming_term_courses_list_card"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Upcoming Planned Courses",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${subjectsInSem.size} course records listed in this plan",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }

                                Button(
                                    onClick = {
                                        courseNameInput = ""
                                        courseCreditsInput = "3"
                                        selectedGradeInput = "A"
                                        showAddCourseDialog = true
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                    modifier = Modifier.height(36.dp).testTag("add_planned_course_btn"),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Add Course", modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Add Course", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))

                            if (subjectsInSem.isEmpty()) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "This upcoming plan is currently empty.",
                                        fontSize = 12.sp,
                                        color = Color.Gray,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "Click 'Add Course' above to include scheduled courses with credit weights and expected grades.",
                                        fontSize = 11.sp,
                                        color = Color.Gray.copy(alpha = 0.8f),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    subjectsInSem.forEach { sub ->
                                        Card(
                                            modifier = Modifier.fillMaxWidth().testTag("planned_course_row_item_${sub.id}"),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.02f)
                                            ),
                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column(modifier = Modifier.weight(1f)) {
                                                    Text(sub.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                        modifier = Modifier.padding(top = 2.dp)
                                                    ) {
                                                        Card(
                                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                                                            shape = RoundedCornerShape(4.dp)
                                                        ) {
                                                            Text(
                                                                text = "${sub.creditHours} Credits",
                                                                fontSize = 9.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                color = MaterialTheme.colorScheme.primary,
                                                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                                            )
                                                        }
                                                        Text(
                                                            text = "Expected: ${sub.grade} (${sub.gpa} GPA)",
                                                            fontSize = 11.sp,
                                                            color = Color.Gray,
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                    }
                                                }

                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    IconButton(
                                                        onClick = {
                                                            editingSubject = sub
                                                            courseNameInput = sub.name
                                                            courseCreditsInput = sub.creditHours.toString()
                                                            selectedGradeInput = sub.grade
                                                        },
                                                        modifier = Modifier.size(32.dp).testTag("edit_course_${sub.id}")
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Edit,
                                                            contentDescription = "Edit Planned Course",
                                                            tint = MaterialTheme.colorScheme.primary,
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                    }

                                                    IconButton(
                                                        onClick = { viewModel.deleteSubject(sub.id) },
                                                        modifier = Modifier.size(32.dp).testTag("delete_course_${sub.id}")
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Default.Delete,
                                                            contentDescription = "Delete Planned Course",
                                                            tint = Color.Red.copy(alpha = 0.7f),
                                                            modifier = Modifier.size(16.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Modal Confirmation Dialog for deleting selected term plan
    if (showSemesterDeleteConfirmation != null) {
        val targetSemId = showSemesterDeleteConfirmation!!
        AlertDialog(
            onDismissRequest = { showSemesterDeleteConfirmation = null },
            title = { Text("Delete Term Plan") },
            text = { Text("Are you sure you want to delete this upcoming planned semester and all of its course records from your forecasting model?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteSemester(targetSemId)
                        if (selectedPlannedSemId == targetSemId) {
                            selectedPlannedSemId = null
                        }
                        showSemesterDeleteConfirmation = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSemesterDeleteConfirmation = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Modal dialog to Add Upcoming course
    if (showAddCourseDialog && selectedPlannedSemId != null) {
        AlertDialog(
            onDismissRequest = { showAddCourseDialog = false },
            title = { Text("Add Upcoming Course") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = courseNameInput,
                        onValueChange = { courseNameInput = it },
                        label = { Text("Course Name") },
                        placeholder = { Text("e.g. Advanced AI Programming") },
                        modifier = Modifier.fillMaxWidth().testTag("planned_course_name_add")
                    )

                    OutlinedTextField(
                        value = courseCreditsInput,
                        onValueChange = { courseCreditsInput = it },
                        label = { Text("Credit Hours") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("planned_course_credits_add")
                    )

                    // Expected grade mappings dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = "Expected Grade: $selectedGradeInput",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { gradeDropdownExpanded = true }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                                }
                            },
                            modifier = Modifier.fillMaxWidth().clickable { gradeDropdownExpanded = true }.testTag("grade_input_dropdown_trigger")
                        )

                        DropdownMenu(
                            expanded = gradeDropdownExpanded,
                            onDismissRequest = { gradeDropdownExpanded = false }
                        ) {
                            availableGrades.forEach { grade ->
                                DropdownMenuItem(
                                    text = { Text(grade) },
                                    onClick = {
                                        selectedGradeInput = grade
                                        gradeDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val name = courseNameInput.ifBlank { "Upcoming Class" }
                        val credits = courseCreditsInput.toIntOrNull() ?: 3
                        viewModel.addSubject(selectedPlannedSemId!!, name, credits, selectedGradeInput)
                        courseNameInput = ""
                        courseCreditsInput = "3"
                        selectedGradeInput = "A"
                        showAddCourseDialog = false
                    },
                    modifier = Modifier.testTag("save_planned_course_confirm")
                ) {
                    Text("Add Course")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCourseDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Modal dialog for editing an existing course record
    if (editingSubject != null) {
        val targetSub = editingSubject!!
        AlertDialog(
            onDismissRequest = { editingSubject = null },
            title = { Text("Edit Planned Course") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = courseNameInput,
                        onValueChange = { courseNameInput = it },
                        label = { Text("Course Name") },
                        modifier = Modifier.fillMaxWidth().testTag("planned_course_name_edit")
                    )

                    OutlinedTextField(
                        value = courseCreditsInput,
                        onValueChange = { courseCreditsInput = it },
                        label = { Text("Credit Hours") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("planned_course_credits_edit")
                    )

                    // Expected grade mappings dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = "Expected Grade: $selectedGradeInput",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { gradeDropdownExpanded = true }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                                }
                            },
                            modifier = Modifier.fillMaxWidth().clickable { gradeDropdownExpanded = true }.testTag("grade_edit_dropdown_trigger")
                        )

                        DropdownMenu(
                            expanded = gradeDropdownExpanded,
                            onDismissRequest = { gradeDropdownExpanded = false }
                        ) {
                            availableGrades.forEach { grade ->
                                DropdownMenuItem(
                                    text = { Text(grade) },
                                    onClick = {
                                        selectedGradeInput = grade
                                        gradeDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val name = courseNameInput.ifBlank { "Upcoming Class" }
                        val credits = courseCreditsInput.toIntOrNull() ?: 3
                        viewModel.updateSubjectDetails(targetSub.id, targetSub.semesterId, name, credits, selectedGradeInput)
                        courseNameInput = ""
                        courseCreditsInput = "3"
                        selectedGradeInput = "A"
                        editingSubject = null
                    },
                    modifier = Modifier.testTag("update_planned_course_confirm")
                ) {
                    Text("Save Changes")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingSubject = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}


// --- TAB 4: CONFIGURATION & TEMPLATES SETTINGS ---
@Composable
fun ConfigurationTemplatesTab(viewModel: AcademicViewModel) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val stats = viewModel.calculateAcademicStats()
    val activeUniversity by viewModel.selectedUniversity.collectAsState()
    val gradeMappings by viewModel.gradeMappings.collectAsState()

    var showCustomUnivDialog by remember { mutableStateOf(false) }
    var newCustomUnivName by remember { mutableStateOf("") }

    // Hardcode simple customizable slots for adding grades to save
    var gradeA_Points by remember { mutableStateOf("4.00") }
    var gradeB_Points by remember { mutableStateOf("3.00") }
    var gradeC_Points by remember { mutableStateOf("2.00") }
    var gradeD_Points by remember { mutableStateOf("1.00") }
    var gradeF_Points by remember { mutableStateOf("0.00") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "HEC Scales & Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Configure grading schemes for HEC, NUST, FAST-NUCES or create custom grading mappings.",
                fontSize = 11.sp,
                color = Color.Gray
            )
        }

        // Theme customizer card (Sleek Interface style)
        item {
            val themePref by viewModel.themePreference.collectAsState()
            Card(
                modifier = Modifier.fillMaxWidth().testTag("app_theme_selection_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Theme",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "App Theme Appearance",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Switch between adaptive light theme and eye-safe twilight dark mode scheme.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val options = listOf(
                            Triple(0, "System", Icons.Default.Settings),
                            Triple(1, "Light", Icons.Default.LightMode),
                            Triple(2, "Dark", Icons.Default.DarkMode)
                        )
                        options.forEach { (value, label, icon) ->
                            val isSelected = themePref == value
                            val containerCol = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent
                            val contentCol = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            val borderStroke = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                            
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(containerCol)
                                    .then(if (borderStroke != null) Modifier.border(borderStroke, RoundedCornerShape(12.dp)) else Modifier)
                                    .clickable { viewModel.setThemePreference(value) }
                                    .padding(vertical = 10.dp, horizontal = 4.dp)
                                    .testTag("theme_option_$label"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = label,
                                        tint = contentCol,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = label,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = contentCol
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Customize Mappings Block
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Active Scale: $activeUniversity", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        TextButton(
                            onClick = { showCustomUnivDialog = true },
                            modifier = Modifier.testTag("create_custom_scheme_action")
                        ) {
                            Text("Create Custom")
                        }
                    }

                    // Display mapped values
                    gradeMappings.forEach { mapping ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Grade: ${mapping.grade}", fontWeight = FontWeight.Medium)
                            Text(text = "${"%.2f".format(mapping.gpa)} cumulative points", color = Color.Gray)
                        }
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                    }
                }
            }
        }

        // Data Backup & Export Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("Records Management & Exporter", fontWeight = FontWeight.Bold)
                    HorizontalDivider()

                    Text(
                        text = "Students can generate progress logs or backup local SQLite configurations to other services.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                // Generate formatted CSV representation
                                val csvText = "HEC GPA Predictor Record\n" +
                                        "University: $activeUniversity\n" +
                                        "Overall CGPA: ${"%.2f".format(stats.completedCgpa)}\n" +
                                        "Completed Credits: ${stats.completedCredits}\n" +
                                        "Forecast CGPA: ${"%.2f".format(stats.predictedCgpa)}"
                                clipboardManager.setText(AnnotatedString(csvText))
                                Toast.makeText(context, "Exported Academic Record CSV to clipboard!", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier.weight(1f).testTag("export_csv_action"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.FileDownload, contentDescription = "CSV")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Share CSV", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                // Generate visual summary Report
                                val reportText = "=====================================\n" +
                                        "   HEC ACADEMIC REPORT CARD\n" +
                                        "=====================================\n" +
                                        "University Scheme: $activeUniversity\n" +
                                        "Computed CGPA: ${"%.2f".format(stats.completedCgpa)}\n" +
                                        "Finished Hours: ${stats.completedCredits}\n" +
                                        "Predicted future: ${"%.2f".format(stats.predictedCgpa)}\n" +
                                        "Scholarship Clearance: ${if (stats.completedCgpa >= 3.0) "APPROVED" else "NOT APPROVED"}\n" +
                                        "Report Generated on Pakistan Federal standard formats.\n" +
                                        "====================================="
                                clipboardManager.setText(AnnotatedString(reportText))
                                Toast.makeText(context, "Academic Report exported to clipboard!", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier.weight(1f).testTag("export_pdf_action"),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.ReceiptLong, contentDescription = "PDF Report")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Share PDF", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    // Modal dialog to compile custom University grading profiles
    if (showCustomUnivDialog) {
        AlertDialog(
            onDismissRequest = { showCustomUnivDialog = false },
            title = { Text("Build Custom Scheme") },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = newCustomUnivName,
                        onValueChange = { newCustomUnivName = it },
                        label = { Text("University Name") },
                        placeholder = { Text("e.g. Iqra University") },
                        modifier = Modifier.fillMaxWidth().testTag("custom_univ_name_input")
                    )

                    Text("Define custom grade values below:", fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = gradeA_Points,
                        onValueChange = { gradeA_Points = it },
                        label = { Text("Grade 'A' Point (HEC default matches 4.0)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("custom_gpa_a")
                    )

                    OutlinedTextField(
                        value = gradeB_Points,
                        onValueChange = { gradeB_Points = it },
                        label = { Text("Grade 'B' Point (HEC default matches 3.0)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("custom_gpa_b")
                    )

                    OutlinedTextField(
                        value = gradeC_Points,
                        onValueChange = { gradeC_Points = it },
                        label = { Text("Grade 'C' Point (HEC default matches 2.0)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("custom_gpa_c")
                    )

                    OutlinedTextField(
                        value = gradeD_Points,
                        onValueChange = { gradeD_Points = it },
                        label = { Text("Grade 'D' Point (HEC default matches 1.0)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("custom_gpa_d")
                    )

                    OutlinedTextField(
                        value = gradeF_Points,
                        onValueChange = { gradeF_Points = it },
                        label = { Text("Grade 'F' Point (HEC default matches 0.0)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth().testTag("custom_gpa_f")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val nameStr = newCustomUnivName.ifBlank { "Custom University Scheme" }
                        val gradesMappedList = listOf(
                            "A" to (gradeA_Points.toDoubleOrNull() ?: 4.0),
                            "A-" to (gradeA_Points.toDoubleOrNull() ?: 3.67).coerceIn(0.0, 4.0) - 0.33,
                            "B+" to (gradeB_Points.toDoubleOrNull() ?: 3.0) + 0.33,
                            "B" to (gradeB_Points.toDoubleOrNull() ?: 3.0),
                            "B-" to (gradeB_Points.toDoubleOrNull() ?: 3.0) - 0.33,
                            "C+" to (gradeC_Points.toDoubleOrNull() ?: 2.0) + 0.33,
                            "C" to (gradeC_Points.toDoubleOrNull() ?: 2.0),
                            "C-" to (gradeC_Points.toDoubleOrNull() ?: 2.0) - 0.33,
                            "D+" to (gradeD_Points.toDoubleOrNull() ?: 1.0) + 0.33,
                            "D" to (gradeD_Points.toDoubleOrNull() ?: 1.0),
                            "F" to (gradeF_Points.toDoubleOrNull() ?: 0.0)
                        )
                        viewModel.insertCustomUniversity(nameStr, gradesMappedList)
                        newCustomUnivName = ""
                        showCustomUnivDialog = false
                    },
                    modifier = Modifier.testTag("save_custom_scheme_confirm")
                ) {
                    Text("Save scale")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCustomUnivDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


// --- TAB 5: PREMIUM INTUITIVE AI ADVISOR ---
@Composable
fun AIAdvisorTab(viewModel: AcademicViewModel) {
    val chatHistory by viewModel.chatMessages.collectAsState()
    val isAdvisorThinking by viewModel.isAdvisorThinking.collectAsState()

    var userMessageInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(chatHistory.size) {
        if (chatHistory.isNotEmpty()) {
            listState.animateScrollToItem(chatHistory.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Chat Section Header information
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "HEC AI Academic Advisor",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Personalized analytical consultation powered by Gemini AI",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            IconButton(
                onClick = { viewModel.clearChatHistory() },
                modifier = Modifier.testTag("clear_chat_button")
            ) {
                Icon(Icons.Default.CleaningServices, contentDescription = "Clear Chat", tint = Color.Gray)
            }
        }

        // Suggestion Chips list
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val templates = listOf(
                "My CGPA is 3.1. How can I reach 3.5 in 2 semesters?",
                "Suggest a study road map to optimize credits",
                "What's the best HEC policy to improve a D grade?"
            )
            templates.forEach { temp ->
                SuggestionChip(
                    onClick = {
                        userMessageInput = temp
                    },
                    label = { Text(temp, fontSize = 10.sp) },
                    modifier = Modifier.testTag("suggestion_chip_${temp.take(10)}")
                )
            }
        }

        HorizontalDivider()

        // Chat bubble lists
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (chatHistory.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ChatBubbleOutline,
                                contentDescription = "Active Bot",
                                modifier = Modifier.size(50.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            )
                            Text(
                                text = "Ask your academic queries above!",
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray
                            )
                            Text(
                                text = "Suggesting recovery paths, degree completion estimates and strategic checklists.",
                                fontSize = 11.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 20.dp)
                            )
                        }
                    }
                }
            }

            items(chatHistory) { msg ->
                val isUser = msg.sender == "user"
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomStart = if (isUser) 16.dp else 0.dp,
                            bottomEnd = if (isUser) 0.dp else 16.dp
                        ),
                        modifier = Modifier
                            .widthIn(max = 280.dp)
                            .testTag(if (isUser) "user_message_bubble" else "advisor_message_bubble")
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (isUser) "Student" else "HEC Academic Advisor",
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = if (isUser) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = msg.content,
                                fontSize = 13.sp,
                                color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            if (isAdvisorThinking) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.widthIn(max = 160.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                                Text("Analyzing record...", fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        // User Input text field row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = userMessageInput,
                onValueChange = { userMessageInput = it },
                placeholder = { Text("Ask academic advisor...") },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                shape = RoundedCornerShape(24.dp)
            )

            IconButton(
                onClick = {
                    if (userMessageInput.isNotBlank()) {
                        viewModel.askAdvisor(userMessageInput)
                        userMessageInput = ""
                    }
                },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .testTag("send_chat_button")
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
