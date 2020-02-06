function initializeCoreMod() {
	return {
		'goal_hook': {
			'target': {
				'type': 'METHOD',
				'class': 'net.minecraft.entity.MobEntity',
				'methodName': '<init>',
				'methodDesc': '(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V'
			},
			'transformer': function(method) {
                patch_MobEntity_Init(method);
                return method;
			}
		},
		'brain_hook': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.entity.LivingEntity',
                'methodName': 'func_213364_a',
                'methodDesc': '(Lcom/mojang/datafixers/Dynamic;)Lnet/minecraft/entity/ai/brain/Brain;'
            },
            'transformer': function(method) {
                patch_LivingEntity_createBrain(method);
                return method;
            }
        }
	};
}

var ASMAPI = Java.type('net.minecraftforge.coremod.api.ASMAPI');
var Opcodes = Java.type('org.objectweb.asm.Opcodes');
var MethodInsnNode = Java.type('org.objectweb.asm.tree.MethodInsnNode');
var InsnNode = Java.type('org.objectweb.asm.tree.InsnNode');
var VarInsnNode = Java.type('org.objectweb.asm.tree.VarInsnNode');
var IntInsnNode = Java.type('org.objectweb.asm.tree.IntInsnNode');
var LabelNode = Java.type('org.objectweb.asm.tree.LabelNode');
var FieldInsnNode = Java.type('org.objectweb.asm.tree.FieldInsnNode');
var TypeInsnNode = Java.type('org.objectweb.asm.tree.TypeInsnNode');
var JumpInsnNode = Java.type('org.objectweb.asm.tree.JumpInsnNode');
var FrameNode = Java.type('org.objectweb.asm.tree.FrameNode');

function patch_MobEntity_Init(method) {
    var foundNode = null;
    var instructions = method.instructions.toArray();
    var length = instructions.length;
    for (var i = 0; i < length; i++) {
        var node = instructions[i];
        if(node.getOpcode() == Opcodes.INVOKEVIRTUAL && node.name.equals(ASMAPI.mapMethod("func_184651_r"))) {
            foundNode = node;
            break;
        }
    }
    if(foundNode !== null) {
        method.instructions.insert(foundNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrcrayfish/improvedwolves/ImprovedWolves", "registerGoals", "(Lnet/minecraft/entity/MobEntity;)V", false));
        method.instructions.insert(foundNode, new VarInsnNode(Opcodes.ALOAD, 0));
        print("[Improved Wolves] Patched registerGoals");
    }
}

function patch_LivingEntity_createBrain(method) {
    var foundNode = null;
    var instructions = method.instructions.toArray();
    var length = instructions.length;
    for (var i = 0; i < length; i++) {
        var node = instructions[i];
        if(node.getOpcode() == Opcodes.NEW && node.desc.equals("net/minecraft/entity/ai/brain/Brain")) {
            foundNode = node;
            break;
        }
    }
    if(foundNode !== null) {
        removeNthNodes(method.instructions, foundNode, 5);
        method.instructions.insert(foundNode, new MethodInsnNode(Opcodes.INVOKESTATIC, "com/mrcrayfish/improvedwolves/ImprovedWolves", "createBrain", "(Lnet/minecraft/entity/LivingEntity;Lcom/mojang/datafixers/Dynamic;)Lnet/minecraft/entity/ai/brain/Brain;", false));
        method.instructions.insert(foundNode, new VarInsnNode(Opcodes.ALOAD, 1));
        method.instructions.insert(foundNode, new VarInsnNode(Opcodes.ALOAD, 0));
        method.instructions.remove(foundNode);
    }
}

function removeNthNodes(instructions, node, n) {
    while(n > 0) {
        if(node.getNext() === null)
            return false;
        instructions.remove(node.getNext());
        n--;
    }
    while(n < 0) {
        if(node.getPrevious() === null)
            return false;
        instructions.remove(node.getPrevious());
        n++;
    }
    return true;
}
