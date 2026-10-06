package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.FastingOrange
import com.example.ui.theme.RenewalEmerald
import com.example.ui.theme.WaterBlue

@Composable
fun LearnScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag("learn_screen")
    ) {
        Text(
            text = "Fasting Science & Guide",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Evidence-based intermittent fasting fundamentals",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Banner Art
        Image(
            painter = painterResource(id = R.drawable.ic_fasting_hero),
            contentDescription = "Metabolic Science Art",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(20.dp))
        )

        Spacer(modifier = Modifier.height(20.dp))

        // What Breaks a Fast Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "What Breaks A Fast?",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Allowed (Keeps You In Fasted State):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = RenewalEmerald
                )
                Spacer(modifier = Modifier.height(4.dp))
                FastItemRow(icon = Icons.Default.CheckCircle, iconTint = RenewalEmerald, title = "Pure Still or Sparkling Water", desc = "Zero calories; critical for kidney filtration")
                FastItemRow(icon = Icons.Default.CheckCircle, iconTint = RenewalEmerald, title = "Black Coffee (No Milk, Sugar)", desc = "Rich in polyphenols, boosts fat oxidation")
                FastItemRow(icon = Icons.Default.CheckCircle, iconTint = RenewalEmerald, title = "Plain Green or Herbal Tea", desc = "EGCG catechin aids satiety and autophagy")
                FastItemRow(icon = Icons.Default.CheckCircle, iconTint = RenewalEmerald, title = "Pure Mineral Electrolytes", desc = "Sodium, potassium, magnesium without sugar")

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Breaks Fast (Triggers Insulin Spike):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(4.dp))
                FastItemRow(icon = Icons.Default.Close, iconTint = MaterialTheme.colorScheme.error, title = "Milk, Creamers, Sugars", desc = "Even 20-30 kcal triggers insulin release")
                FastItemRow(icon = Icons.Default.Close, iconTint = MaterialTheme.colorScheme.error, title = "Protein Shakes & BCAAs", desc = "Amino acids activate mTOR, halting autophagy")
                FastItemRow(icon = Icons.Default.Close, iconTint = MaterialTheme.colorScheme.error, title = "Fruit Juices & Smoothies", desc = "Concentrated fructose immediately halts fat burn")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // How to break a fast safely
        ExpandableGuideCard(
            title = "How To Safely Break Your Fast",
            icon = Icons.Default.School,
            summary = "Gentle refeeding protects your stomach from digestive discomfort after 16+ hours.",
            details = "1. Start small: Begin with a light bone broth, a handful of raw nuts, or sliced cucumber.\n" +
                    "2. Wait 20-30 minutes before having your main meal.\n" +
                    "3. Prioritize whole protein and fiber over refined carbs to prevent blood sugar spikes and crashes.\n" +
                    "4. Avoid heavy deep-fried or overly spicy meals as your first post-fast food."
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Electrolytes guide
        ExpandableGuideCard(
            title = "Electrolytes & Avoiding Headaches",
            icon = Icons.Default.WaterDrop,
            summary = "When insulin drops during fasting, the kidneys excrete sodium more rapidly.",
            details = "• If you feel fatigue, lightheadedness, or mild headaches ('fasting flu'), you likely need sodium.\n" +
                    "• Add a pinch of sea salt or Himalayan pink salt to your water.\n" +
                    "• Ensure adequate magnesium (e.g. glycinate) in the evening.\n" +
                    "• Stay consistent with hydration—aim for at least 2 to 2.5 liters of water daily."
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Safety disclaimer
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.LocalHospital,
                    contentDescription = "Medical",
                    tint = FastingOrange,
                    modifier = Modifier.size(20.dp).padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Column {
                    Text(
                        text = "Safety & Medical Note",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Intermittent fasting is intended for healthy adults. If you are pregnant, nursing, under 18, taking blood sugar medications, or have a history of eating disorders, please consult your physician before undertaking extended fasting.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 100% Free & Privacy Commitment Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Privacy",
                    tint = RenewalEmerald,
                    modifier = Modifier.size(20.dp).padding(top = 2.dp)
                )
                Spacer(modifier = Modifier.size(10.dp))
                Column {
                    Text(
                        text = "100% Free & Privacy-First",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "FastFlow is completely free and supported by non-intrusive ads (no expensive subscriptions or paywalls). All your fast logs, hydration entries, and personal notes remain 100% private and stored locally on your device.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun FastItemRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: androidx.compose.ui.graphics.Color,
    title: String,
    desc: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(18.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ExpandableGuideCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    summary: String,
    details: String
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.size(10.dp))
                    Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = summary, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Text(text = details, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}
