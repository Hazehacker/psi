/* eslint-disable */
// @ts-ignore
import * as API from './types';

export function displayNucleusEnum(field: API.NucleusEnum) {
  return { 0: 'NUMBER_0', 1: 'NUMBER_1', 2: 'NUMBER_2' }[field];
}

export function displayNucleusEnum2(field: API.NucleusEnum2) {
  return { 0: 'NUMBER_0', 1: 'NUMBER_1', 2: 'NUMBER_2' }[field];
}

export function displayStateEnum(field: API.StateEnum) {
  return { 0: 'NUMBER_0', 1: 'NUMBER_1', 2: 'NUMBER_2', 3: 'NUMBER_3' }[field];
}

export function displayStateEnum2(field: API.StateEnum2) {
  return { 0: 'NUMBER_0', 1: 'NUMBER_1', 2: 'NUMBER_2', 3: 'NUMBER_3' }[field];
}

export function displayTypeEnum(field: API.TypeEnum) {
  return { 采购单: '采购单', 采购退货单: '采购退货单' }[field];
}

export function displayTypeEnum2(field: API.TypeEnum2) {
  return { 0: 'NUMBER_0', 1: 'NUMBER_1' }[field];
}

export function displayTypeEnum3(field: API.TypeEnum3) {
  return { 0: 'NUMBER_0', 1: 'NUMBER_1' }[field];
}
