// src/store/useUserStroe.js
import { create } from "zustand";
import { persist } from "zustand/middleware";

export const SET_CURRENT_USER = "SET_CURRENT_USER";
export const CLEAR_CURRENT_USER = "CLEAR_CURRENT_USER";

const useUserStore = create(
  persist(
    (set) => ({
      user: null,     //현재 로그인 한 사람은 딱 한명

      // 로그인 시
      setUser: (user) =>     //로그인 시 백엔드에서 받은 정보 저장
        set({
          user,
          lastAction: SET_CURRENT_USER,
        }),

      // 로그아웃 시
      clearUser: () =>        //유저박스 비우기
        set({
          user: null,
          lastAction: CLEAR_CURRENT_USER,
        }),

        // 재로그인 없이 credit만 업데이트
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
//persist로 새로고침해도 로그인 안 풀림
export default useUserStore;
