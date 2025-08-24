import type Ingredient from "./Ingredient"

export default interface Recipe {
    id: string
    title: string
    summary?: string
    steps?: string
    tags?: string[]
    ingredients: Ingredient[]
}