import { IUser } from "./user-model"
import { IGroupWrapper } from "../group/group-wrapper-model"

export interface IUserWrapper {
  user: IUser
  groupsWrapper: IGroupWrapper[]
}
