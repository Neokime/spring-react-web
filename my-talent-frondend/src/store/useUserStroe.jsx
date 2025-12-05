import { create } from 'zustand';
import { persist } from 'zustand/middleware';

// Redux 액션 타입 대체 — 문자열로 유지 가능
export const SET_CURRENT_USER = 'SET_CURRENT_USER';
export const CLEAR_CURRENT_USER = 'CLEAR_CURRENT_USER';

const useUserStore = create(
  persist(
    (set) => ({
      user: null,

      // Redux의 setCurrentUser 액션 대체
      setCurrentUser: (user) =>
        set({
          user,
          lastAction: SET_CURRENT_USER,
        }),

      // Redux의 clearCurrentUser 액션 대체
      clearCurrentUser: () =>
        set({
          user: null,
          lastAction: CLEAR_CURRENT_USER,
        }),
    }),
    {
      name: 'currentUser', // localStorage key
    }
  )
);

export default useUserStore;