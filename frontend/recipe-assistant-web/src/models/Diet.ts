export type Diet = 'VEGETARIAN' |
    'VEGAN' |
    'PESCETARIAN' |
    'KETO' |
    'PALEO' |
    'MEDITERRANEAN' |
    'LOW_CARB' |
    'LOW_FAT' |
    'DAIRY_FREE' |
    'GLUTEN_FREE' |
    'NUT_FREE' |
    'NONE' |
    'KOSHER'

export function getName(diet: Diet) {
    switch(diet) {
        case "VEGAN": return 'Vegana'
        case 'VEGETARIAN': return 'Vegetariana'
        case 'LOW_FAT': return 'Baja en grasas'
        case 'GLUTEN_FREE': return 'Sin gluten'
        case 'LOW_CARB': return 'Baja en carbohidratos'
        case 'MEDITERRANEAN': return 'Mediterraea'
        case 'NUT_FREE': return 'Sin nuez'
        case 'PESCETARIAN': return 'Piscifora'
        case 'DAIRY_FREE': return'Sin lactosa'
        case 'KETO': return 'Keto'
        case 'KOSHER': return 'Kosher'
        case 'PALEO': return 'Paleolitica'
        case 'NONE':
        default:
            return 'Ninguna'
    }
}