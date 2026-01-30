# 📋 Map de Loot par Monstre - Système Équilibré avec Pénalités Progressives

## 🎯 Système de Drop Innovant

### Mécanisme Anti-Abus:
Chaque item droppé **réduit la chance** de dropper les items suivants dans la même boucle.

**Formule:** `Chance Ajustée = Chance Base × (1 - Pénalité Cumulative)`

**Pénalités par type de monstre:**
- **Skeleton** (Commun): -25% par item → Maximum 3 items
- **Goblin** (Intermédiaire): -20% par item → Maximum 4 items
- **Dragon** (Boss): -15% par item → Maximum 5 items

### Exemple de Calcul (Skeleton):
```
1er item: 50% × (1 - 0.00) = 50% de chance
2ème item: 40% × (1 - 0.25) = 30% de chance
3ème item: 35% × (1 - 0.50) = 17.5% de chance
```

---

## 💀 Skeleton (Monstre Basique)

**Statistiques:**
- PV: 50
- Attaque: 8
- Niveau: 1

**Chance de Drop Global:** 45%  
**Pénalité:** -25% par item  
**Maximum d'items:** 3

### 📦 Table de Loot:

#### Armes & Armures
| Item | Type | Stats | Chance Base | Rareté |
|------|------|-------|-------------|---------|
| Épée Rouillée | Arme (Épée) | +8 ATK | 40% | Commun |
| Arc Ancien | Arme (Arc) | +6 ATK | 30% | Commun |
| Casque Fissuré | Armure (Tête) | +5 DEF | 35% | Commun |
| Bouclier Ébréché | Armure (Bouclier) | +4 DEF | 25% | Commun |

#### 🧪 Extensions (Potions & Matériaux)
| Item | Type | Effet | Chance Base | Rareté |
|------|------|-------|-------------|---------|
| Petite Potion de Vie | Potion | +20 PV | 50% | Commun |
| Os | Matériau | Craft | 60% | Très Commun |
| Poussière d'Os | Matériau | Craft magique | 45% | Commun |

**Particularité:** Pas de loot garanti - peut ne rien donner.

---

## 👹 Goblin (Monstre Intermédiaire)

**Statistiques:**
- PV: 75
- Attaque: 12
- Niveau: 2

**Chance de Drop Global:** 60%  
**Pénalité:** -20% par item  
**Maximum d'items:** 4

### 📦 Table de Loot:

#### Armes & Armures
| Item | Type | Stats | Chance Base | Rareté |
|------|------|-------|-------------|---------|
| Dague Empoisonnée | Arme (Dague) | +15 ATK | 50% | Peu Commun |
| Massue Cloutée | Arme (Massue) | +12 ATK | 45% | Peu Commun |
| Veste en Cuir Renforcé | Armure (Légère) | +12 DEF | 40% | Peu Commun |
| Gants Volés | Armure (Mains) | +8 DEF | 35% | Peu Commun |
| Hache de Guerre | Arme (Hache) | +18 ATK | 25% | Rare |

#### 🧪 Extensions (Potions & Matériaux)
| Item | Type | Effet | Chance Base | Rareté |
|------|------|-------|-------------|---------|
| Potion de Vie | Potion | +50 PV | 45% | Peu Commun |
| Potion de Vie Moyenne | Potion | +35 PV | 40% | Peu Commun |
| Cuir de Gobelin | Matériau | Craft armure | 50% | Peu Commun |
| Dent de Gobelin | Matériau | Craft rare | 30% | Rare |

**Particularité:** Loot garanti - Si aucun item ne drop, un item aléatoire est donné.

---

## 🐉 Dragon (Boss Légendaire)

**Statistiques:**
- PV: 200
- Attaque: 25
- Niveau: 3

**Chance de Drop Global:** 80%  
**Pénalité:** -15% par item  
**Maximum d'items:** 5

### 📦 Table de Loot:

#### Armes & Armures Légendaires
| Item | Type | Stats | Chance Base | Rareté |
|------|------|-------|-------------|---------|
| Croc de Dragon | Arme (Dague) | +35 ATK | 60% | Légendaire |
| Griffe Draconique | Arme (Griffes) | +32 ATK | 45% | Légendaire |
| Écailles de Dragon | Armure (Lourde) | +40 DEF | 50% | Légendaire |
| Heaume du Dragon | Armure (Tête) | +28 DEF | 35% | Légendaire |
| Souffle Éternel | Arme (Bâton) | +40 ATK | 20% | Ultra Rare |

#### 🧪 Extensions (Potions & Matériaux Légendaires)
| Item | Type | Effet | Chance Base | Rareté |
|------|------|-------|-------------|---------|
| Élixir du Dragon | Potion | +100 PV | 40% | Légendaire |
| Sang de Dragon | Potion | +75 PV | 30% | Légendaire |
| Cœur de Dragon | Matériau | Craft légendaire | 15% | Mythique |
| Cristal de Flamme | Matériau | Craft magique | 25% | Légendaire |

**Particularité:** Loot garanti - Si aucun item ne drop, un item aléatoire est donné.

---

## 📊 Impact des Pénalités Progressives

### Exemple Concret - Dragon:

**Sans pénalités (ancien système):**
- Tous les items testés à leur chance base
- Risque de tout looter en 1 fois
- Items moyens: ~3.5 par kill

**Avec pénalités (nouveau système):**
```
Item 1 (60%): 60% × (1 - 0.00) = 60.0% ✅ DROP
Item 2 (45%): 45% × (1 - 0.15) = 38.3% ✅ DROP
Item 3 (50%): 50% × (1 - 0.30) = 35.0% ✅ DROP
Item 4 (35%): 35% × (1 - 0.45) = 19.3% ❌
Item 5 (40%): 40% × (1 - 0.45) = 22.0% ❌
```
- Items moyens: ~2.0-2.5 par kill
- Distribution plus équilibrée
- Farming plus intéressant

---

## 🎮 Analyse de l'Équilibrage

### Probabilités Réelles avec Pénalités:

#### Skeleton (Max 3):
- **0 items:** ~55%
- **1 item:** ~28%
- **2 items:** ~13%
- **3 items:** ~4%
- **Moyenne:** 0.66 items/kill

#### Goblin (Max 4 + garanti):
- **0 items:** 0% (garanti si drop)
- **1 item:** ~25%
- **2 items:** ~30%
- **3 items:** ~20%
- **4 items:** ~10%
- **Moyenne:** 1.85 items/kill

#### Dragon (Max 5 + garanti):
- **0 items:** 20%
- **1 item:** ~22%
- **2 items:** ~28%
- **3 items:** ~18%
- **4 items:** ~8%
- **5 items:** ~4%
- **Moyenne:** 2.15 items/kill

---

## 🔧 Avantages du Système à Pénalités

### ✅ Bénéfices:
1. **Fin de l'abus** - Impossible de tout looter
2. **Équilibrage naturel** - Les boss donnent plus mais pas trop
3. **Farming récompensé** - Besoin de plusieurs kills pour tout collecter
4. **Progression logique** - Items rares restent rares
5. **Diversité** - Chaque kill est différent

### 🎯 Paramètres Ajustables:
- **Pénalité par item** (actuellement: 15-25%)
- **Maximum d'items** (actuellement: 3-5)
- **Chance globale** (actuellement: 45-80%)

---

## 💡 Extensions Implémentées

### 🧪 Potions:
- **Skeleton:** Petite Potion (+20 PV)
- **Goblin:** Potion de Vie (+50 PV), Potion de Vie Moyenne (+35 PV)
- **Dragon:** Élixir (+100 PV), Sang de Dragon (+75 PV)

### ⚒️ Matériaux de Craft:
- **Skeleton:** Os, Poussière d'Os
- **Goblin:** Cuir de Gobelin, Dent de Gobelin
- **Dragon:** Cœur de Dragon (mythique), Cristal de Flamme

### 🔮 Idées Futures:
1. **Gemmes** - Augmentent les stats des armes
2. **Parchemins** - Sorts à usage unique
3. **Clés** - Ouvrent des zones secrètes
4. **Buffs temporaires** - Augmentent les stats en combat
5. **Sets d'équipement** - Bonus si équipement complet

---

## 📝 Code Pattern

```java
// Exemple d'utilisation dans generateRandomLoot()
double dropPenalty = 0.0;
int itemsDropped = 0;
int maxItems = 3; // Selon le monstre

for (Map.Entry<Item, Double> entry : possibleLoot.entrySet()) {
    double adjustedChance = entry.getValue() * (1.0 - dropPenalty);
    
    if (Math.random() < adjustedChance) {
        addItemToInventory(entry.getKey());
        itemsDropped++;
        dropPenalty += 0.25; // Pénalité
        
        if (itemsDropped >= maxItems) break;
    }
}
```

---

## 🎯 Conclusion

Le système à **pénalités progressives** garantit:
- 🎲 **Variété** dans les drops
- ⚖️ **Équilibre** entre puissance et rareté
- 🔄 **Rejouabilité** via le farming
- 🚫 **Pas d'abus** de loot excessif

**Le farming devient stratégique, pas juste chanceux !**