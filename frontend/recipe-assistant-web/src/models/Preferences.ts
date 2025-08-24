import type { Diet } from "./Diet";

export default interface Preferences {
    diet: Diet
    excludes: string[]
    allergens: string[]
}