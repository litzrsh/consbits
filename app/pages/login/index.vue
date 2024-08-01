<script setup lang="ts">
// Define page meta
definePageMeta({
  layout: "anonymous",
});

/**
 * =====================================================
 * Declare interfaces
 * =====================================================
 */
interface ILoginForm {
  username: string | null;
  password: string | null;
  remember: boolean;
}

/**
 * =====================================================
 * Declare variables
 * =====================================================
 */
const data = ref<ILoginForm>({
  username: null,
  password: null,
  remember: false,
});

/**
 * =====================================================
 * Declare references
 * =====================================================
 */
const usernameRef = ref<HTMLInputElement>();
const passwordRef = ref<HTMLInputElement>();

/**
 * =====================================================
 * Event loop handler
 * =====================================================
 */
onMounted(function () {
  if (usernameRef.value) {
    usernameRef.value.focus();
  }
});
</script>

<template>
  <div class="login">
    <div class="container">
      <form class="form">
        <div class="logo"></div>
        <div class="field">
          <input
            ref="usernameRef"
            type="text"
            v-model="data.username"
            required
            placeholder="Username"
          />
        </div>
        <div class="field">
          <input
            ref="passwordRef"
            type="password"
            v-model="data.password"
            required
            placeholder="Password"
          />
        </div>
        <div class="field">
          <label class="checkbox">
            <input type="checkbox" v-model="data.remember" />
            Remember me
          </label>
        </div>
        <div class="buttons">
          <button type="submit" class="button is-fullwidth">Login</button>
        </div>
      </form>
    </div>
  </div>
</template>
