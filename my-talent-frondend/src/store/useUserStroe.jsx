// src/store/useUserStroe.js
import { create } from "zustand";
import { persist } from "zustand/middleware";

export const SET_CURRENT_USER = "SET_CURRENT_USER";
export const CLEAR_CURRENT_USER = "CLEAR_CURRENT_USER";

const useUserStore = create(
  persist(
    (set) => ({
      user: null,

      // 로그인 시
      setUser: (user) =>
        set({
          user,
          lastAction: SET_CURRENT_USER,
        }),

      // 로그아웃 시
      clearUser: () =>
        set({
          user: null,
          lastAction: CLEAR_CURRENT_USER,
        }),

        // credit만 업데이트
      updateCredit: (credit) =>
        set((state) => ({
          user: {
            ...state.user,
            credit,
       },
  })),

    }),
    {
      name: "currentUser", // localStorage key
    }
  )
);

export default useUserStore;
