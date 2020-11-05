import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IStatusCode, defaultValue } from 'app/shared/model/status-code.model';

export const ACTION_TYPES = {
  FETCH_STATUSCODE_LIST: 'statusCode/FETCH_STATUSCODE_LIST',
  FETCH_STATUSCODE: 'statusCode/FETCH_STATUSCODE',
  CREATE_STATUSCODE: 'statusCode/CREATE_STATUSCODE',
  UPDATE_STATUSCODE: 'statusCode/UPDATE_STATUSCODE',
  DELETE_STATUSCODE: 'statusCode/DELETE_STATUSCODE',
  RESET: 'statusCode/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IStatusCode>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type StatusCodeState = Readonly<typeof initialState>;

// Reducer

export default (state: StatusCodeState = initialState, action): StatusCodeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_STATUSCODE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_STATUSCODE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_STATUSCODE):
    case REQUEST(ACTION_TYPES.UPDATE_STATUSCODE):
    case REQUEST(ACTION_TYPES.DELETE_STATUSCODE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_STATUSCODE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_STATUSCODE):
    case FAILURE(ACTION_TYPES.CREATE_STATUSCODE):
    case FAILURE(ACTION_TYPES.UPDATE_STATUSCODE):
    case FAILURE(ACTION_TYPES.DELETE_STATUSCODE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_STATUSCODE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_STATUSCODE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_STATUSCODE):
    case SUCCESS(ACTION_TYPES.UPDATE_STATUSCODE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_STATUSCODE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/status-codes';

// Actions

export const getEntities: ICrudGetAllAction<IStatusCode> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_STATUSCODE_LIST,
  payload: axios.get<IStatusCode>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IStatusCode> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_STATUSCODE,
    payload: axios.get<IStatusCode>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IStatusCode> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_STATUSCODE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IStatusCode> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_STATUSCODE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IStatusCode> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_STATUSCODE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
