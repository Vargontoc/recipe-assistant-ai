<template>
  <div class="w-full">
    <!-- Input wrapper -->
    <div class="relative">
      <!-- Label -->
      <label 
        v-if="label" 
        :for="inputId"
        class="block text-sm font-medium text-gray-700 mb-2"
        :class="{ 'text-red-700': hasError }"
      >
        {{ label }}
        <span v-if="required" class="text-red-500 ml-1">*</span>
      </label>

      <!-- Input field -->
      <div class="relative">
        <!-- Prefix icon -->
        <div v-if="prefixIcon" class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
          <component :is="prefixIcon" class="h-5 w-5 text-gray-400" />
        </div>

        <!-- Main input -->
        <input
          :id="inputId"
          :type="type"
          :value="modelValue"
          :placeholder="placeholder"
          :required="required"
          :disabled="disabled"
          :class="inputClasses"
          @input="handleInput"
          @blur="handleBlur"
          @focus="handleFocus"
        />

        <!-- Suffix icon -->
        <div v-if="suffixIcon || showClearButton" class="absolute inset-y-0 right-0 pr-3 flex items-center">
          <!-- Clear button -->
          <button
            v-if="showClearButton && modelValue"
            @click="clearInput"
            type="button"
            class="text-gray-400 hover:text-gray-600 transition-colors"
          >
            <svg class="h-4 w-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path>
            </svg>
          </button>
          <!-- Suffix icon -->
          <component v-else-if="suffixIcon" :is="suffixIcon" class="h-5 w-5 text-gray-400" />
        </div>
      </div>

      <!-- Loading indicator -->
      <div v-if="loading" class="absolute inset-y-0 right-0 pr-3 flex items-center">
        <div class="animate-spin h-4 w-4 border-2 border-blue-600 border-t-transparent rounded-full"></div>
      </div>
    </div>

    <!-- Help text and validation -->
    <div class="mt-2 min-h-[20px]">
      <!-- Error message -->
      <p v-if="hasError" class="text-sm text-red-600 flex items-center">
        <svg class="h-4 w-4 mr-1 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.732-.833-2.5 0L4.232 16.5c-.77.833.192 2.5 1.732 2.5z"></path>
        </svg>
        {{ errorMessage }}
      </p>
      
      <!-- Help text -->
      <p v-else-if="helpText" class="text-sm text-gray-500">
        {{ helpText }}
      </p>

      <!-- Character count -->
      <p v-if="showCharCount && maxLength" class="text-xs text-gray-400 text-right">
        {{ String(modelValue || '').length }} / {{ maxLength }}
      </p>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed, ref } from 'vue'

interface Props {
  modelValue?: string | number;
  label?: string;
  type?: 'text' | 'email' | 'password' | 'number' | 'tel' | 'url';
  placeholder?: string;
  required?: boolean;
  disabled?: boolean;
  loading?: boolean;
  helpText?: string;
  errorMessage?: string;
  maxLength?: number;
  showCharCount?: boolean;
  showClearButton?: boolean;
  prefixIcon?: string;
  suffixIcon?: string;
  validationRules?: Array<(value: any) => string | true>;
}

const props = withDefaults(defineProps<Props>(), {
  type: 'text',
  placeholder: '',
  required: false,
  disabled: false,
  loading: false,
  helpText: '',
  errorMessage: '',
  showCharCount: false,
  showClearButton: false,
  validationRules: () => []
});

const emit = defineEmits<{
  'update:modelValue': [value: string | number];
  'blur': [event: FocusEvent];
  'focus': [event: FocusEvent];
  'clear': [];
}>();

// Internal state
const isFocused = ref(false);
const inputId = `input-${Math.random().toString(36).substr(2, 9)}`;

// Validation
const hasError = computed(() => Boolean(props.errorMessage));

// Input classes
const inputClasses = computed(() => {
  const baseClasses = [
    'block w-full rounded-lg border transition-colors duration-200',
    'focus:ring-2 focus:ring-offset-0 focus:outline-none',
    'disabled:opacity-50 disabled:cursor-not-allowed'
  ];

  if (props.prefixIcon) {
    baseClasses.push('pl-10');
  } else {
    baseClasses.push('pl-4');
  }

  if (props.suffixIcon || props.showClearButton || props.loading) {
    baseClasses.push('pr-10');
  } else {
    baseClasses.push('pr-4');
  }

  baseClasses.push('py-3');

  if (hasError.value) {
    baseClasses.push('border-red-300 focus:border-red-500 focus:ring-red-500');
  } else if (isFocused.value) {
    baseClasses.push('border-blue-300 focus:border-blue-500 focus:ring-blue-500');
  } else {
    baseClasses.push('border-gray-300 focus:border-blue-500 focus:ring-blue-500');
  }

  return baseClasses.join(' ');
});

// Event handlers
const handleInput = (event: Event) => {
  const target = event.target as HTMLInputElement;
  let value: string | number = target.value;
  
  if (props.type === 'number') {
    value = Number(value);
  }
  
  emit('update:modelValue', value);
};

const handleBlur = (event: FocusEvent) => {
  isFocused.value = false;
  emit('blur', event);
};

const handleFocus = (event: FocusEvent) => {
  isFocused.value = true;
  emit('focus', event);
};

const clearInput = () => {
  emit('update:modelValue', '');
  emit('clear');
};
</script>
